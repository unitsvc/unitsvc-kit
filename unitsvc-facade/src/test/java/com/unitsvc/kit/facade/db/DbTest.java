package com.unitsvc.kit.facade.db;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.unitsvc.kit.facade.datasource.table.ColumnInfo;
import com.unitsvc.kit.facade.datasource.table.TableInfo;
import com.unitsvc.kit.facade.datasource.table.annotation.ColumnAnno;
import com.unitsvc.kit.facade.datasource.table.annotation.TableAnno;
import com.unitsvc.kit.facade.datasource.table.enums.ColumnTypeEnum;
import com.jpardus.spider.sccs.Log;
import com.unitsvc.kit.core.diff.enums.DiffFieldTypeEnum;
import lombok.Data;
import org.apache.commons.lang.StringUtils;
import org.junit.Test;

import java.io.Serializable;
import java.lang.reflect.Field;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 功能描述：
 *
 * @author : jun
 * @version : v1.0.0
 * @since : 2023/6/29 18:20
 **/
public class DbTest {

    @Data
    @TableAnno(tableName = "temp_table", enableSession = true)
    public static class TempTable implements Serializable {

        private static final long serialVersionUID = -2565279332502136047L;

        /**
         * 主键
         */
        @ColumnAnno(isPrimary = true, columnName = "_id", columnAlias = "id", columnType = ColumnTypeEnum.STRING)
        private String id;

        /**
         * 数据列
         */
        @ColumnAnno(columnName = "nameCn", columnType = ColumnTypeEnum.STRING)
        private String nameCn;

        private String nameEn;

        private long number;

        private Date time;
    }

    private static final String EXCLUDE_FIELD = "serialVersionUID";

    @SuppressWarnings("all")
    private <T> TableInfo clazzToTableInfo(Class<T> schemaClazz) {
        // 获取所有注解
        TableAnno tableAnno = schemaClazz.getAnnotation(TableAnno.class);
        // 原始主键
        String primaryId = "";
        String primaryAlias = "";
        if (null != tableAnno) {
            // 是否启用会话字段
            boolean enableSession = Boolean.TRUE.equals(tableAnno.enableSession());
            List<ColumnInfo> columns = new ArrayList<>();
            Field[] fields = schemaClazz.getDeclaredFields();
            for (Field field : fields) {
                ColumnAnno annotation = field.getAnnotation(ColumnAnno.class);
                if (null != annotation) {
                    // 若启用会话，则主键注解不生效，会额外生成新的主键
                    if (annotation.isPrimary()) {
                        primaryId = annotation.columnName();
                        if (StringUtils.isNotEmpty(annotation.columnAlias())) {
                            primaryAlias = annotation.columnAlias();
                        } else {
                            primaryAlias = annotation.columnName();
                        }
                    }
                    ColumnInfo columnInfo = new ColumnInfo();
                    columnInfo.setIsPrimary(!enableSession && annotation.isPrimary());
                    columnInfo.setColumnName(annotation.columnName());
                    // 处理实体类名称
                    if (StringUtils.isNotEmpty(annotation.columnAlias())) {
                        columnInfo.setColumnAlias(annotation.columnAlias());
                    } else {
                        columnInfo.setColumnAlias(annotation.columnName());
                    }
                    columnInfo.setColumnType(annotation.columnType());
                    columns.add(columnInfo);
                } else {
                    if (tableAnno.enableAuto()) {
                        // 若注解不存在，则自动生成列名
                        String columnName = field.getName();
                        if (!EXCLUDE_FIELD.equals(columnName)) {
                            Class<?> type = field.getType();
                            // 获取字段类型
                            String typeName = field.getType().getName();
                            // 根据字段类型进行处理
                            if (DiffFieldTypeEnum.STRING.getTypeName().equals(typeName)) {
                                // 字符串类型
                                ColumnInfo columnInfo = ColumnInfo.builder()
                                        .isPrimary(false).columnName(columnName).columnAlias(columnName)
                                        .columnType(ColumnTypeEnum.STRING).build();
                                columns.add(columnInfo);
                            } else if (DiffFieldTypeEnum.SQL_TIMESTAMP.getTypeName().equals(typeName)) {
                                // 时间戳类型，PASS 忽略
                            } else if (DiffFieldTypeEnum.WRAPPER_LONG.getTypeName().equals(typeName) || Long.TYPE == type) {
                                // 长整形类型
                                ColumnInfo columnInfo = ColumnInfo.builder()
                                        .isPrimary(false).columnName(columnName).columnAlias(columnName)
                                        .columnType(ColumnTypeEnum.LONG).build();
                                columns.add(columnInfo);
                            } else if (DiffFieldTypeEnum.WRAPPER_INTEGER.getTypeName().equals(typeName) || Integer.TYPE == type) {
                                // 整形类型
                                ColumnInfo columnInfo = ColumnInfo.builder()
                                        .isPrimary(false).columnName(columnName).columnAlias(columnName)
                                        .columnType(ColumnTypeEnum.INTEGER).build();
                                columns.add(columnInfo);
                            } else if (DiffFieldTypeEnum.WRAPPER_BOOLEAN.getTypeName().equals(typeName) || Boolean.TYPE == type) {
                                // 布尔类型
                                ColumnInfo columnInfo = ColumnInfo.builder()
                                        .isPrimary(false).columnName(columnName).columnAlias(columnName)
                                        .columnType(ColumnTypeEnum.BOOLEAN).build();
                                columns.add(columnInfo);
                            } else if (DiffFieldTypeEnum.BIG_DECIMAL.getTypeName().equals(typeName)) {
                                // 高精度类型
                                ColumnInfo columnInfo = ColumnInfo.builder()
                                        .isPrimary(false).columnName(columnName).columnAlias(columnName)
                                        .columnType(ColumnTypeEnum.NUMBER).build();
                                columns.add(columnInfo);
                            } else if (DiffFieldTypeEnum.WRAPPER_BYTE.getTypeName().equals(typeName) || Byte.TYPE == type) {
                                // 字节类型，PASS 忽略
                            } else if (DiffFieldTypeEnum.WRAPPER_SHORT.getTypeName().equals(typeName) || Short.TYPE == type) {
                                // 短整形类型
                                ColumnInfo columnInfo = ColumnInfo.builder()
                                        .isPrimary(false).columnName(columnName).columnAlias(columnName)
                                        .columnType(ColumnTypeEnum.INTEGER).build();
                                columns.add(columnInfo);
                            } else if (DiffFieldTypeEnum.WRAPPER_FLOAT.getTypeName().equals(typeName) || Float.TYPE == type) {
                                // 单浮点类型
                                ColumnInfo columnInfo = ColumnInfo.builder()
                                        .isPrimary(false).columnName(columnName).columnAlias(columnName)
                                        .columnType(ColumnTypeEnum.FLOAT).build();
                                columns.add(columnInfo);
                            } else if (DiffFieldTypeEnum.WRAPPER_DOUBLE.getTypeName().equals(typeName) || Double.TYPE == type) {
                                // 双浮点类型
                                ColumnInfo columnInfo = ColumnInfo.builder()
                                        .isPrimary(false).columnName(columnName).columnAlias(columnName)
                                        .columnType(ColumnTypeEnum.DOUBLE).build();
                                columns.add(columnInfo);
                            } else if (DiffFieldTypeEnum.DATE.getTypeName().equals(typeName)) {
                                // 日期类型
                                ColumnInfo columnInfo = ColumnInfo.builder()
                                        .isPrimary(false).columnName(columnName).columnAlias(columnName)
                                        .columnType(ColumnTypeEnum.DATETIME).build();
                                columns.add(columnInfo);
                            } else if (DiffFieldTypeEnum.JSON_PRIMITIVE.getTypeName().equals(typeName)) {
                                // JSON常规类型，PASS 忽略
                            } else if (DiffFieldTypeEnum.JSON_OBJECT.getTypeName().equals(typeName) || DiffFieldTypeEnum.JSON_ARRAY.getTypeName().equals(typeName)) {
                                // JSON复杂类型，PASS 忽略
                            } else if (DiffFieldTypeEnum.JSON_ELEMENT.getTypeName().equals(typeName)) {
                                // JSON所有类型，PASS 忽略
                            }
                        }
                    }
                }
            }
            // 若启用会话字段
            if (enableSession) {
                // 生成会话key，即主键
                ColumnInfo primaryColumn = new ColumnInfo();
                primaryColumn.setIsPrimary(true);
                primaryColumn.setColumnName(tableAnno.sessionKey());
                primaryColumn.setColumnAlias(tableAnno.sessionKey());
                primaryColumn.setColumnType(ColumnTypeEnum.STRING);
                columns.add(primaryColumn);
                // 生成会话ID
                ColumnInfo sessionColumn = new ColumnInfo();
                sessionColumn.setIsPrimary(false);
                sessionColumn.setColumnName(tableAnno.sessionId());
                sessionColumn.setColumnAlias(tableAnno.sessionId());
                sessionColumn.setColumnType(ColumnTypeEnum.STRING);
                columns.add(sessionColumn);
            }
            TableInfo tableInfo = new TableInfo();
            // 数据表
            tableInfo.setTableName(tableAnno.tableName());
            tableInfo.setPrimaryId(primaryId);
            tableInfo.setPrimaryAlias(primaryAlias);
            tableInfo.setEnableSession(tableAnno.enableSession());
            tableInfo.setSessionKey(tableAnno.sessionKey());
            tableInfo.setSessionId(tableAnno.sessionId());
            // 数据列
            tableInfo.setColumns(columns);
            return tableInfo;
        }
        Log.error(String.format("无有效数据表注解【%s|%s】，请检查后再试", "@TableAnno", "@ColumnAnno"));
        return null;
    }

    @Test
    public void test() {
        TableInfo tableInfo = clazzToTableInfo(TempTable.class);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        System.out.println("gson.toJson(tableInfo) = " + gson.toJson(tableInfo));
    }

}
