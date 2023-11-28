package com.unitsvc.kit.core.diff;

import com.google.gson.*;
import com.unitsvc.kit.core.diff.annotation.DiffField;
import com.unitsvc.kit.core.diff.annotation.DiffId;
import com.unitsvc.kit.core.diff.enums.DiffFieldTypeEnum;
import com.unitsvc.kit.core.diff.model.DiffWrapper;
import com.unitsvc.kit.core.diff.utils.DiffUtil;
import org.apache.commons.lang.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 功能描述：复杂对象比对抽象方法
 *
 * @author : jun
 * @version : v1.0.0
 * @since : 2023/6/27 18:21
 */
public abstract class AbstractObjectDiff {

    /**
     * 序列化
     */
    private static final Gson GSON = new GsonBuilder().serializeNulls().disableHtmlEscaping().create();

    /**
     * 自定义查询比对
     *
     * @param sourceObject 原始对象
     * @param targetObject 目标对象
     * @return 字符串
     * @throws Exception 异常
     */
    protected abstract String genDiffStr(Object sourceObject, Object targetObject) throws Exception;

    /**
     * 生成中文差异
     *
     * @param sourceObject 原始对象
     * @param targetObject 目标对象
     * @return 字符串
     * @throws Exception 异常
     */
    public static String genChineseDiffStr(Object sourceObject, Object targetObject) throws Exception {
        List<DiffWrapper> diffWrappers = generateDiff(sourceObject, targetObject);
        return DiffUtil.genDiffStr(diffWrappers);
    }

    /**
     * 比对差异
     *
     * @param sourceObject 原始对象
     * @param targetObject 目标对象
     * @return 差异
     * @throws Exception 异常
     */
    public static List<DiffWrapper> generateDiff(Object sourceObject, Object targetObject) throws Exception {
        String typeName = "";
        if (null != sourceObject) {
            typeName = sourceObject.getClass().getTypeName();
        } else if (null != targetObject) {
            typeName = targetObject.getClass().getTypeName();
        }
        return generateDiff("", "", typeName, sourceObject, targetObject);
    }

    /**
     * 比对差异
     *
     * @param path         路径
     * @param typeName     类型名称
     * @param cnName       中文名称
     * @param sourceObject 原始对象
     * @param targetObject 目标对象
     * @return
     * @throws Exception 异常
     */
    private static List<DiffWrapper> generateDiff(String path, String cnName, String typeName, Object sourceObject, Object targetObject) throws Exception {
        // 过滤都为空的数据
        if (sourceObject == null && targetObject == null) {
            return null;
        }
        List<DiffWrapper> diffWrappers = new ArrayList<>();
        if (sourceObject == null || targetObject == null) {
            // 处理某一个为空的数据
            DiffWrapper diffWrapper = DiffUtil
                    .getDiffWrapper(
                            path, cnName, typeName,
                            (sourceObject == null ? null : toObjectString(sourceObject)),
                            (targetObject == null ? null : toObjectString(targetObject))
                    );
            diffWrappers.add(diffWrapper);
            return diffWrappers;
        }
        // 先判断object类型
        if (!sourceObject.getClass().getName().equals(targetObject.getClass().getName())) {
            return null;
        }
        if (sourceObject.hashCode() == targetObject.hashCode()) {
            return null;
        }
        // 获取目标对象字段
        Field[] fields = sourceObject.getClass().getDeclaredFields();
        for (final Field field : fields) {
            // 获取字段类型
            Class<?> type = field.getType();
            String newPath = path + "/" + field.getName();
            String nameCn = newPath;
            field.setAccessible(true);
            if (field.isAnnotationPresent(DiffField.class)) {
                DiffField logVO = field.getAnnotation(DiffField.class);
                if (cnName == null || "".equals(cnName)) {
                    // 根节点直接取注解名称
                    nameCn = logVO.name();
                } else {
                    // 子节点进行字段拼接
                    nameCn = cnName + "." + logVO.name();
                }
                if (logVO.ignore()) {
                    // 忽略该比对字段
                    continue;
                }
            } else {
                continue;
            }
            // 字段为集合类型
            if (Collection.class.isAssignableFrom(type)) {
                // ------------------------------ 集合 ------------------------------------
                // 先判断一下集合
                List<?> oldList = (List) field.get(sourceObject);
                List<?> newList = (List) field.get(targetObject);
                Map<Object, Object> oldFilterMap = new HashMap<>(16);
                Map<Object, Object> newFilterMap = new HashMap<>(16);
                Class<?> genericClass = null;
                if (field.getGenericType() instanceof ParameterizedType) {
                    // 获取泛型Class
                    genericClass = (Class<?>) ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0];
                }
                if (genericClass == null) {
                    continue;
                }
                // 获取泛型集合字段
                Field[] collectionFields = genericClass.getDeclaredFields();
                Field keyField = null;
                String keyNameCn = "";
                for (Field collectionField : collectionFields) {
                    if (collectionField.isAnnotationPresent(DiffId.class)) {
                        keyField = collectionField;
                        DiffId keyFieldAnnotation = keyField.getAnnotation(DiffId.class);
                        // 集合主键字段名称，只取第一个
                        keyNameCn = keyFieldAnnotation.name();
                        break;
                    }
                }
                if (keyField == null) {
                    // 集合存在主键注解 @DiffId 时才进行比对
                    continue;
                }
                keyField.setAccessible(true);
                if (newList != null) {
                    for (Object newObj : newList) {
                        Object value = keyField.get(newObj);
                        newFilterMap.put(value, newObj);
                    }
                }
                if (oldList != null) {
                    for (Object oldObj : oldList) {
                        Object value = keyField.get(oldObj);
                        oldFilterMap.put(value, oldObj);
                    }
                }
                // 获取集合旧值集合
                Set<Object> oldKeySets = oldFilterMap.keySet();
                // 获取集合新值集合
                Set<Object> newKeySets = newFilterMap.keySet();
                Set<Object> resultSet = new HashSet<>();
                resultSet.addAll(oldKeySets);
                resultSet.addAll(newKeySets);
                // 取两个之间的并集,然后统一输出
                for (Object result : resultSet) {
                    Object oldObj = oldFilterMap.get(result);
                    Object newObj = newFilterMap.get(result);
                    String opPath = newPath + "/" + keyField.getName() + "[" + result.toString() + "]";
                    String opNameCn = nameCn + "." + keyNameCn + "[" + result + "]";
                    // 递归处理集合字段差异
                    List<DiffWrapper> collectDiff = generateDiff(opPath, opNameCn, genericClass.getTypeName(), oldObj, newObj);
                    if (collectDiff != null) {
                        diffWrappers.addAll(collectDiff);
                    }
                }
                resultSet.clear();
            } else {
                // ---------------------------------- 普通属性 --------------------------------------------------
                // 递归出口
                // 比对单个字段差异
                DiffWrapper diffWrapper = generateOneDiffs(newPath, nameCn, field, sourceObject, targetObject);
                if (diffWrapper != null) {
                    diffWrappers.add(diffWrapper);
                }
            }
        }
        return diffWrappers;
    }

    /**
     * 比对单个字段差异
     *
     * @param path   字段路径
     * @param nameCn 字段名称
     * @param field  字段
     * @param source 原始对象
     * @param target 目标对象
     * @return 差异
     * @throws Exception 异常
     */
    private static DiffWrapper generateOneDiffs(String path, String nameCn, Field field, Object source, Object target) throws Exception {
        // 获取字段类型名称，示例：java.lang.Long
        String typeName = field.getType().getName();
        // 获取字段类型
        Class<?> type = field.getType();
        field.setAccessible(true);
        DiffField logVO = field.getAnnotation(DiffField.class);
        String dateFormat = "";
        if (logVO != null) {
            dateFormat = logVO.dateFormat();
            // 过滤忽略的字段key
            if (logVO.ignore()) {
                return null;
            }
        }
        // 根据字段类型进行处理
        if (DiffFieldTypeEnum.STRING.getTypeName().equals(typeName)) {
            // 字符串类型
            String oldStr = (String) field.get(source);
            String newStr = (String) field.get(target);
            return DiffUtil.get(path, nameCn, typeName, oldStr, newStr);
        } else if (DiffFieldTypeEnum.SQL_TIMESTAMP.getTypeName().equals(typeName)) {
            // 时间戳类型
            DateFormat format = new SimpleDateFormat(StringUtils.isBlank(dateFormat) ? "yyyy-MM-dd HH:mm:ss" : dateFormat);
            java.sql.Timestamp newTime = (java.sql.Timestamp) field.get(target);
            java.sql.Timestamp oldTime = (java.sql.Timestamp) field.get(source);
            String newTempTimeStr = "";
            String oldTimeTimeStr = "";
            if (newTime != null) {
                newTempTimeStr = format.format(newTime);
            }
            if (oldTime != null) {
                oldTimeTimeStr = format.format(oldTime);
            }
            if (oldTime == newTime && oldTime == null) {
                return null;
            }
            if (oldTime == null || newTime == null) {
                return DiffUtil.getDiffWrapper(path, nameCn, typeName, oldTime == null ? null : oldTimeTimeStr, newTime == null ? null : newTempTimeStr);
            }

            if (!StringUtils.equals(newTempTimeStr, oldTimeTimeStr)) {
                return DiffUtil.getDiffWrapper(path, nameCn, typeName, format.format(oldTime), format.format(newTime));
            }
        } else if (DiffFieldTypeEnum.WRAPPER_LONG.getTypeName().equals(typeName) || Long.TYPE == type) {
            // 长整形类型
            Long oldValue = (Long) field.get(source);
            Long newValue = (Long) field.get(target);
            return DiffUtil.get(path, nameCn, typeName, oldValue, newValue);

        } else if (DiffFieldTypeEnum.WRAPPER_INTEGER.getTypeName().equals(typeName) || Integer.TYPE == type) {
            // 整形类型
            Integer oldValue = (Integer) field.get(source);
            Integer newValue = (Integer) field.get(target);
            return DiffUtil.get(path, nameCn, typeName, oldValue, newValue);

        } else if (DiffFieldTypeEnum.WRAPPER_BOOLEAN.getTypeName().equals(typeName) || Boolean.TYPE == type) {
            // 布尔类型
            Boolean oldValue = field.getBoolean(source);
            Boolean newValue = field.getBoolean(target);
            return DiffUtil.get(path, nameCn, typeName, oldValue, newValue);

        } else if (DiffFieldTypeEnum.BIG_DECIMAL.getTypeName().equals(typeName)) {
            // 高精度类型
            BigDecimal oldValue = (BigDecimal) field.get(source);
            BigDecimal newValue = (BigDecimal) field.get(target);
            if (oldValue != null && newValue != null && oldValue.compareTo(newValue) == 0) {
                newValue = oldValue;
            }
            return DiffUtil.get(path, nameCn, typeName, oldValue, newValue);
        } else if (DiffFieldTypeEnum.WRAPPER_BYTE.getTypeName().equals(typeName) || Byte.TYPE == type) {
            // 字节类型
            // 预留不处理
            Byte oldValue = (Byte) field.get(source);
            Byte newValue = (Byte) field.get(target);
            if (oldValue != null && newValue != null && oldValue.compareTo(newValue) == 0) {
                newValue = oldValue;
            }
            return DiffUtil.get(path, nameCn, typeName, oldValue, newValue);
        } else if (DiffFieldTypeEnum.WRAPPER_SHORT.getTypeName().equals(typeName) || Short.TYPE == type) {
            // 短整形类型
            Short oldValue = (Short) field.get(source);
            Short newValue = (Short) field.get(target);
            if (oldValue != null && newValue != null && oldValue.compareTo(newValue) == 0) {
                newValue = oldValue;
            }
            return DiffUtil.get(path, nameCn, typeName, oldValue, newValue);
            // 预留不处理 有需要在处理
        } else if (DiffFieldTypeEnum.WRAPPER_FLOAT.getTypeName().equals(typeName) || Float.TYPE == type) {
            // 单浮点类型
            Float oldValue = field.getFloat(source);
            Float newValue = field.getFloat(target);
            return DiffUtil.get(path, nameCn, typeName, oldValue, newValue);

        } else if (DiffFieldTypeEnum.WRAPPER_DOUBLE.getTypeName().equals(typeName) || Double.TYPE == type) {
            // 双浮点类型
            String oldValue = field.get(source) == null ? null : String.valueOf(field.get(source));
            String newValue = field.get(target) == null ? null : String.valueOf(field.get(target));
            return DiffUtil.get(path, nameCn, typeName, oldValue, newValue);

        } else if (DiffFieldTypeEnum.DATE.getTypeName().equals(typeName)) {
            // 日期类型
            DateFormat format = new SimpleDateFormat(StringUtils.isBlank(dateFormat) ? "yyyy-MM-dd" : dateFormat);
            java.util.Date newTime = (java.util.Date) field.get(target);
            java.util.Date oldTime = (java.util.Date) field.get(source);
            String newTempTimeStr = "";
            String oldTimeTimeStr = "";
            if (newTime != null) {
                newTempTimeStr = format.format(newTime);
            }
            if (oldTime != null) {
                oldTimeTimeStr = format.format(oldTime);
            }
            if (oldTime == newTime && oldTime == null) {
                return null;
            }
            if (oldTime == null || newTime == null) {
                return DiffUtil.getDiffWrapper(path, nameCn, typeName, oldTime == null ? null : oldTimeTimeStr, newTime == null ? null : newTempTimeStr);
            }
            if (!StringUtils.equals(newTempTimeStr, oldTimeTimeStr)) {
                return DiffUtil.getDiffWrapper(path, nameCn, typeName, oldTimeTimeStr, newTempTimeStr);
            }
        } else if (DiffFieldTypeEnum.JSON_PRIMITIVE.getTypeName().equals(typeName)) {
            // JSON常规类型
            String oldValue = field.get(source) == null ? null : String.valueOf(field.get(source));
            String newValue = field.get(target) == null ? null : String.valueOf(field.get(target));
            return DiffUtil.get(path, nameCn, typeName, oldValue, newValue);
        } else if (DiffFieldTypeEnum.JSON_OBJECT.getTypeName().equals(typeName) || DiffFieldTypeEnum.JSON_ARRAY.getTypeName().equals(typeName)) {
            // JSON复杂类型
            String oldValue = field.get(source) == null ? null : GSON.toJson(field.get(source));
            String newValue = field.get(target) == null ? null : GSON.toJson(field.get(target));
            return DiffUtil.get(path, nameCn, typeName, oldValue, newValue);
        } else if (DiffFieldTypeEnum.JSON_ELEMENT.getTypeName().equals(typeName)) {
            // JSON所有类型
            String oldValue = null;
            String newValue = null;
            if (source instanceof JsonPrimitive) {
                oldValue = field.get(source) == null ? null : String.valueOf(field.get(source));
            } else {
                oldValue = field.get(source) == null ? null : GSON.toJson(field.get(source));
            }
            if (target instanceof JsonPrimitive) {
                newValue = field.get(target) == null ? null : String.valueOf(field.get(target));
            } else {
                newValue = field.get(target) == null ? null : GSON.toJson(field.get(target));
            }
            return DiffUtil.get(path, nameCn, typeName, oldValue, newValue);
        }
        return null;
    }

    /**
     * 转换为对象字符串
     *
     * @param source 对象
     * @return
     */
    private static String toObjectString(Object source) {
        if (null != source) {
            String json = GSON.toJson(source);
            JsonElement jsonElement = GSON.fromJson(json, JsonElement.class);
            if (jsonElement instanceof JsonPrimitive) {
                return String.valueOf(source);
            } else {
                return json;
            }
        }
        return null;
    }

    /**
     * 获取对象字符串
     *
     * @param source 原始对象
     * @return 字符串
     * @throws Exception 异常
     */
    @Deprecated
    private static String getObjectString(Object source) throws Exception {
        if (source == null) {
            return "";
        }
        List<String> logList = new ArrayList<>();
        Field[] fields = source.getClass().getDeclaredFields();
        for (Field field : fields) {
            String logStr = "";
            String typeName = field.getType().getName();
            field.setAccessible(true);
            DiffField logVO = field.getAnnotation(DiffField.class);
            String nameCn = field.getName();
            String dateFormat = "";
            if (logVO != null) {
                nameCn = logVO.name();
                dateFormat = logVO.dateFormat();
            }
            if (DiffFieldTypeEnum.STRING.getTypeName().equals(typeName)) {
                String oldStr = (String) field.get(source);
                logStr = "[" + nameCn + "]=" + oldStr + " ";
                logList.add(logStr);
            } else if (DiffFieldTypeEnum.DATE.getTypeName().equals(typeName)) {
                DateFormat format = new SimpleDateFormat(StringUtils.isBlank(dateFormat) ? "yyyy-MM-dd HH:mm:ss" : dateFormat);
                java.util.Date oldTime = (java.util.Date) field.get(source);
                if (oldTime != null) {
                    logStr = "[" + nameCn + "]=" + format.format(oldTime) + " ";
                    logList.add(logStr);
                }
            } else {
                Object oldValue = (Object) field.get(source);
                logStr = "[" + nameCn + "]=" + oldValue.toString() + " ";
                logList.add(logStr);
            }
        }
        return StringUtils.join(logList.iterator(), ",");
    }

}
