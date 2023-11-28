package com.unitsvc.kit.facade.generate.utils;

import cn.hutool.core.date.DateUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.unitsvc.kit.facade.generate.dtos.TableInfo;
import com.unitsvc.kit.facade.generate.schema.field.FieldConfig;
import com.jpardus.spider.sccs.Log;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.OutputStreamWriter;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Date;
import java.util.List;

/**
 * 功能描述：数据表实体生成工具
 * <p>
 * 说明：自动化工具帮助类
 * <pre>
 * 方式一：
 *        // 文件输出路径
 *         String outputPath = "./";
 *         // 程序包名称
 *         String packageName = "com.unitsvc.kit.code.model";
 *         // 类名后缀
 *         String classPostfix = "PO";
 *         // 数据表名称（待生成实体类的数据表名称）
 *         String tableName = "table_xxx_record";
 *         // 生成实体类
 *         UniGenTableInfoUtil.outputTableInfoClass(outputPath, tableName, packageName, classPostfix, false, false, new IUniTableService() {
 *             @Override
 *             public void beforeStart() {
 *                 // pass 权限控制
 *             }
 *
 *             @Override
 *             public void afterFinally() {
 *                 // pass 清理权限
 *             }
 *
 *             @Override
 *             public TableInfo getTableInfo(String tableName) {
 *                 // pass
 *                 return UniGenTableInfoUtil.toTableInfo(null);
 *             }
 *         });
 * </pre>
 *
 * <pre>
 * 方式二：
 *         // 生成数据表集合
 *         List<String> tableNames = Lists.newArrayList(
 *                 "table_xxx_record"
 *         );
 *         // 生成目标包名称
 *         String packageName = "com.unitsvc.kit.code.model";
 *         // 生成不带PO实体类
 *         UniGenTableInfoUtil.createTableInfoClassHasPO(tableNames, packageName, tableName -> {
 *             try {
 *                 Auth.addAuthInfo("16307");
 *                 // 获取数据表定义
 *                 ITableService tableService = FacadeClient.getRemoteObject(ITableService.class);
 *                 return UniGenTableInfoUtil.toTableInfo(tableService.getTableInfo(tableName));
 *             } finally {
 *                 Auth.removeAuthInfo();
 *             }
 *         });
 * </pre>
 *
 * @author : coder
 * @version : v1.0.0
 * @since : 2023/11/7 15:42
 **/
@SuppressWarnings("all")
public class UniGenTableInfoUtil {

    /**
     * 序列化
     */
    public static final Gson GSON_NULL = new GsonBuilder().disableHtmlEscaping().create();

    /**
     * 转换成数据表实体类
     *
     * @param obj
     * @return
     */
    public static TableInfo toTableInfo(Object obj) {
        if (null == obj) {
            return null;
        }
        return GSON_NULL.fromJson(GSON_NULL.toJson(obj), TableInfo.class);
    }

    /**
     * 创建数据表实体类
     *
     * @param tableNameList 表名集合
     * @param packageName   包名称
     */
    public static void createTableInfoClassNoPO(List<String> tableNameList, String packageName, IUniTableService tableService) {
        createTableInfoClass(tableNameList, packageName, "", tableService);
    }

    /**
     * 创建数据表实体类
     *
     * @param tableNameList 表名集合
     * @param packageName   包名称
     */
    public static void createTableInfoClassHasPO(List<String> tableNameList, String packageName, IUniTableService tableService) {
        createTableInfoClass(tableNameList, packageName, "PO", tableService);
    }

    /**
     * 创建数据表实体类
     *
     * @param tableNameList 表名集合
     * @param packageName   包名称
     * @param classPostfix  后缀
     */
    public static void createTableInfoClass(List<String> tableNameList, String packageName, String classPostfix, IUniTableService tableService) {
        for (String tableName : tableNameList) {
            // 文件输出路径
            String outputPath = String.format("./src/main/java/%s", packageName.replace(".", "/"));
            // 生成实体类
            outputTableInfoClass(outputPath, tableName, packageName, classPostfix, false, false, tableService);
        }
    }

    /**
     * 生成数据表实体类
     * <p>
     * 说明：不支持复杂对象生成
     *
     * @param outputPath       输出路径
     * @param tableName        数据表名
     * @param packageName      包名称
     * @param classPostfix     类前缀
     * @param enableTableAnno  是否启用注解
     * @param enableBigDecimal 是否启用精度
     */
    public static void outputTableInfoClass(String outputPath, String tableName, String packageName, String classPostfix, boolean enableTableAnno, boolean enableBigDecimal, IUniTableService tableService) {
        try {
            // 调用前执行方法
            tableService.beforeStart();
            // 获取数据表定义
            TableInfo tableInfo = tableService.getTableInfo(tableName);
            Log.info(String.format("*** [0/1] [%s/%s] [实体类创建] [开始]***", tableInfo.getId(), tableInfo.getName()));
            // 输出实体类
            String className = buildTableInfoClass(tableInfo, outputPath, packageName, classPostfix, enableTableAnno, enableBigDecimal);
            Log.debug(String.format("*** [1/1] [%s/%s] [实体类创建] [完成，温馨提示：若数据集时间字段是Integer类型的时间，则需要手动映射为String或Long类型，否则会产生精度丢失。] ***", tableInfo.getId(), tableInfo.getName()));
            Log.info(String.format("*** [1/1] [%s/%s] [实体类创建] [完成，生成 %s 实体类]***", tableInfo.getId(), tableInfo.getName(), className));
        } catch (Exception e) {
            Log.error(String.format("*** [1/1] [%s] [实体类创建] [异常：%s]***", tableName, e.getMessage()), e);
        } finally {
            // 执行后置执行方法
            tableService.afterFinally();
        }
    }

    /**
     * 构造数据表实体类
     *
     * @param tableInfo        数据表
     * @param outputPath       输出路径
     * @param packageName      程序包名称，示例：com.open.code.easytool.model
     * @param classPostfix     类名称后缀，示例：PO
     * @param enableTableAnno  是否启用数据表列注解
     * @param enableBigDecimal 是否启用高精度数值
     * @return 文件类名
     * @throws Exception
     */
    public static String buildTableInfoClass(TableInfo tableInfo, String outputPath, String packageName, String classPostfix, boolean enableTableAnno, boolean enableBigDecimal) throws Exception {
        // 创建时间，用于生成注释
        String since = DateUtil.format(new Date(), "yyyy/M/d HH:mm");
        // java文件名称
        String className = StringUtils.isNotEmpty(classPostfix) ? toFirstUpperCamel(tableInfo.getId()) + classPostfix : toFirstUpperCamel(tableInfo.getId());
        File file = new File(outputPath + "/" + className + ".java");
        // -------------------------------------------- 解决路径不存在问题 ------------------------------------------------
        try {
            File parentFile = file.getParentFile();
            if (!parentFile.exists()) {
                boolean mkdirs = parentFile.mkdirs();
                Log.debug(String.format("*** [1] [%s/%s] [实体类创建] [开始，创建父级目录 %s ，结果：%s]***", tableInfo.getId(), tableInfo.getName(), outputPath, mkdirs));
            }
            if (!file.exists()) {
                boolean newFile = file.createNewFile();
                Log.debug(String.format("*** [1] [%s/%s] [实体类创建] [开始，创建目录文件 %s ，结果：%s]***", tableInfo.getId(), tableInfo.getName(), className + ".java", newFile));
            }
        } catch (Exception e) {
            Log.debug(String.format("*** [1] [%s/%s] [实体类创建] [文件操作异常：%s]***", tableInfo.getId(), tableInfo.getName(), e.getMessage()), e);
        }
        // -------------------------------------------------------------------------------------------------------------
        BufferedWriter bw = new BufferedWriter(
                new OutputStreamWriter(Files.newOutputStream(file.toPath()), StandardCharsets.UTF_8)
        );
        // 数据表名称
        String tableName = tableInfo.getId();
        // 包名
        bw.write(String.format("package %s;", packageName));
        // 依赖
        bw.write("\r\n");
        bw.write("\r\n");
        bw.write("import com.google.gson.annotations.SerializedName;");
        bw.write("\r\n");
        bw.write("import com.jpardus.db.jmongo.MongoCollectionName;");
        bw.write("\r\n");
        bw.write("import com.jpardus.db.jmongo.MongoId;");
        bw.write("\r\n");
        if (enableTableAnno) {
            bw.write("import com.unitsvc.kit.facade.datasource.table.annotation.ColumnAnno;");
            bw.write("\r\n");
            bw.write("import com.unitsvc.kit.facade.datasource.table.annotation.TableAnno;");
            bw.write("\r\n");
            bw.write("import com.unitsvc.kit.facade.datasource.table.enums.ColumnTypeEnum;");
            bw.write("\r\n");
        }
        bw.write("import lombok.AllArgsConstructor;");
        bw.write("\r\n");
        bw.write("import lombok.Builder;");
        bw.write("\r\n");
        bw.write("import lombok.Data;");
        bw.write("\r\n");
        bw.write("import lombok.NoArgsConstructor;");
        bw.write("\r\n");
        bw.write("import lombok.experimental.Accessors;");
        bw.write("\r\n");
        bw.write("\r\n");
        bw.write("import java.io.Serializable;");
        bw.write("\r\n");
        if (enableBigDecimal) {
            bw.write("import java.math.BigDecimal;");
            bw.write("\r\n");
            bw.write("\r\n");
        } else {
            bw.write("\r\n");
        }
        // 注释
        bw.write("/**");
        bw.write("\r\n");
        bw.write(" * 功能描述：" + tableInfo.getName());
        bw.write("\r\n");
        bw.write(" *");
        bw.write("\r\n");
        bw.write(" * @author : coder");
        bw.write("\r\n");
        bw.write(" * @version : v1.0.0");
        bw.write("\r\n");
        bw.write(" * @since : " + since);
        bw.write("\r\n");
        bw.write(" **/");
        bw.write("\r\n");
        // 注解
        bw.write("@Data");
        bw.write("\r\n");
        bw.write("@AllArgsConstructor");
        bw.write("\r\n");
        bw.write("@NoArgsConstructor");
        bw.write("\r\n");
        bw.write("@Builder");
        bw.write("\r\n");
        bw.write("@Accessors(chain = true)");
        bw.write("\r\n");
        bw.write(String.format("@MongoCollectionName(\"%s\")", tableName));
        bw.write("\r\n");
        if (enableTableAnno) {
            bw.write(String.format("@TableAnno(tableName = \"%s\", enableSession = true)", tableName));
            bw.write("\r\n");
        }
        // 类名
        bw.write(String.format("public class %s implements Serializable {", className));
        bw.write("\r\n");
        bw.write("\r\n");
        bw.write("\tprivate static final long serialVersionUID = -1L;");
        bw.write("\r\n");
        bw.write("\r\n");
        List<FieldConfig> columns = tableInfo.getColumns();
        for (FieldConfig column : columns) {
            String fieldD = null;
            if ("_id".equals(column.getId())) {
                fieldD = "private " + toDataType(column.getType().name(), enableBigDecimal) + " " + "id" + ";";
            } else {
                fieldD = "private " + toDataType(column.getType().name(), enableBigDecimal) + " " + column.getId() + ";";
            }
            // 名称
            String name = column.getName();
            if (StringUtils.isNotEmpty(name)) {
                bw.write("\t/**");
                bw.write("\r\n");
                bw.write("\t * " + name);
                bw.write("\r\n");
                // 描述
                String description = column.getDescription();
                if (StringUtils.isNotEmpty(description)) {
                    bw.write("\t * " + "<p>");
                    bw.write("\r\n");
                    bw.write("\t * " + "说明：" + description);
                    bw.write("\r\n");
                }
                bw.write("\t */");
                bw.write("\r\n");
            }
            if ("_id".equals(column.getId())) {
                bw.write("\t");
                bw.write("@MongoId");
                bw.write("\r\n");
                bw.write("\t");
                bw.write("@SerializedName(\"_id\")");
                bw.write("\r\n");
                String columnType = "ColumnTypeEnum.STRING";
                String type = toDataType(column.getType().name(), enableBigDecimal);
                if (null != type) {
                    switch (type) {
                        case "Integer":
                            columnType = "ColumnTypeEnum.INTEGER";
                            break;
                        case "String":
                            columnType = "ColumnTypeEnum.STRING";
                            break;
                        default:
                            // pass
                    }
                }
                if (enableTableAnno) {
                    bw.write("\t");
                    bw.write(String.format("@ColumnAnno(isPrimary = true, columnName = \"_id\", columnAlias = \"id\", columnType = %s)", columnType));
                    bw.write("\r\n");
                }
            }
            bw.write("\t");
            bw.write(fieldD);
            bw.write("\r\n");
        }
        bw.write("}");
        bw.flush();
        bw.close();
        return className;
    }

    /**
     * 转换数据类型
     *
     * @param fieldType        数据类型
     * @param enableBigDecimal 是否启用精度
     * @return
     */
    private static String toDataType(String fieldType, boolean enableBigDecimal) {
        switch (fieldType) {
            case "STRING":
                // 字符串
            case "ENUM":
                // 枚举
                return "String";
            case "DOUBLE":
                // 小数
                if (enableBigDecimal) {
                    return "BigDecimal";
                } else {
                    return "Double";
                }
            case "INTEGER":
                // 整数
                return "Integer";
            case "BOOLEAN":
                // 布尔
                return "Boolean";
            default:
                // pass
        }
        Log.error(String.format("*** [1] [%s] [实体类创建] [当前数据类型暂不支持]***", fieldType));
        // 若返回null，则当前属性不支持
        return null;
    }

    /**
     * 下划线转驼峰，首字母大写
     *
     * @param str 字符串
     * @return 首字母大写名称
     */
    public static String toFirstUpperCamel(String str) {
        String[] strings = str.split("_");
        StringBuilder stringBuffer = new StringBuilder();
        for (String string : strings) {
            stringBuffer.append(string.substring(0, 1).toUpperCase()).append(string.substring(1).toLowerCase());
        }
        return stringBuffer.toString();
    }

    /**
     * 查询数据表接口
     */
    public interface IUniTableService {
        /**
         * 可选，前置条件
         * <p>
         * 说明：调用方法前处理方式
         */
        default void beforeStart() {
        }

        /**
         * 可选，后置条件
         * <p>
         * 说明：调用方法后资源清理，即使异常也会执行
         */
        default void afterFinally() {
        }

        /**
         * 必填，获取表下的列集合
         *
         * @param tableName 表名
         * @return
         */
        TableInfo getTableInfo(String tableName);

    }

}
