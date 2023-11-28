package com.unitsvc.kit.core.diff.utils;

import com.unitsvc.kit.core.diff.model.DiffWrapper;
import com.unitsvc.kit.core.diff.model.Difference;

import java.util.List;

/**
 * 功能描述：复杂对象比对工具类
 *
 * @author : jun
 * @version : v1.0.0
 * @since : 2023/6/27 18:21
 */
public class DiffUtil {

    /**
     * 修改
     */
    public final static String CHANGE = "CHANGE";

    /**
     * 删除元素
     */
    public final static String REMOVE = "REMOVE";

    /**
     * 新增元素
     */
    public final static String ADD = "ADD";

    /**
     * 获取字段差异
     *
     * @param path     路径
     * @param nameCn   中文名称
     * @param typeName 类型名称
     * @param oldValue 旧值
     * @param newValue 新值
     * @param <T>      泛型
     * @return
     */
    public static <T> DiffWrapper get(String path, String nameCn, String typeName, T oldValue, T newValue) {
        if (oldValue == newValue && oldValue == null) {
            return null;
        }
        if (oldValue == null || newValue == null) {
            return getDiffWrapper(path, nameCn, typeName, oldValue, newValue);
        }
        if (!newValue.equals(oldValue)) {
            return getDiffWrapper(path, nameCn, typeName, oldValue, newValue);
        }
        return null;
    }

    /**
     * 构造数据变更记录
     *
     * @param path     路径
     * @param nameCn   中文名称
     * @param typeName 类型名称
     * @param oldStr   旧值
     * @param newStr   新值
     * @return
     */
    public static DiffWrapper getDiffWrapper(String path, String nameCn, String typeName, Object oldStr, Object newStr) {
        String op = CHANGE;
        if (newStr == null && oldStr != null) {
            // 删除元素
            op = REMOVE;
        }
        if (oldStr == null && newStr != null) {
            // 新增元素
            op = ADD;
        }
        return new DiffWrapper(path, nameCn, typeName, op, new Difference(oldStr, newStr));
    }

    /**
     * 构造中文比对差异
     *
     * @param diffWrapperList 差异数据
     * @return
     */
    public static String genDiffStr(List<DiffWrapper> diffWrapperList) {
        // 线程安全
        StringBuffer sb = new StringBuffer();
        if (diffWrapperList != null && diffWrapperList.size() > 0) {
            for (DiffWrapper diffWrapper : diffWrapperList) {
                String op = diffWrapper.getOperate();
                String opCn = "修改为";
                if (op.equals(ADD)) {
                    opCn = "被添加成";
                    sb.append(String.format("「%s」%s[%s]", diffWrapper.getName(), opCn, diffWrapper.getDiffValue().getNewValue()));
                } else if (op.equals(REMOVE)) {
                    opCn = "被删除";
                    sb.append(String.format("「%s」由[%s]%s", diffWrapper.getName(), diffWrapper.getDiffValue().getOldValue(), opCn));
                } else {
                    sb.append(String.format("「%s」由[%s]%s[%s]", diffWrapper.getName(), diffWrapper.getDiffValue().getOldValue(), opCn, diffWrapper.getDiffValue().getNewValue()));
                }
                sb.append("\n");
            }
        }
        return sb.toString();
    }

}
