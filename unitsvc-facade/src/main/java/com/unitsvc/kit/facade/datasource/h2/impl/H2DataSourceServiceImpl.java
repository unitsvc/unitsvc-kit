package com.unitsvc.kit.facade.datasource.h2.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.*;
import com.unitsvc.kit.facade.datasource.table.annotation.ColumnAnno;
import com.unitsvc.kit.facade.datasource.table.annotation.TableAnno;
import com.unitsvc.kit.facade.datasource.table.enums.ColumnTypeEnum;
import com.jpardus.spider.sccs.Log;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.row.Db;
import com.mybatisflex.core.row.Row;
import com.unitsvc.kit.core.diff.enums.DiffFieldTypeEnum;
import com.unitsvc.kit.facade.datasource.h2.H2DataSourceService;
import com.unitsvc.kit.facade.datasource.table.ColumnInfo;
import com.unitsvc.kit.facade.datasource.table.TableInfo;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.Statement;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 功能描述：H2数据源服务
 *
 * <pre>
 * 使用方式：
 * // @Autowired(required = false)
 * // private H2DataSourceService h2DataSourceService;
 * </pre>
 *
 * @author : jun
 * @version : v1.0.0
 * @since : 2023/6/20 10:08
 **/
@Service
public class H2DataSourceServiceImpl implements H2DataSourceService {

    /**
     * 序列化
     * <p>
     * 警告：String序列化为Date，在不同语言环境下会产生问题，故默认固定时间格式。
     */
    private static final Gson GSON = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd HH:mm:ss")
            .disableHtmlEscaping().serializeNulls().create();

    /**
     * H2数据源
     */
    @Resource(name = "h2DruidDataSource")
    private DataSource h2DruidDataSource;

    /**
     * 执行schemeSql语句
     *
     * <pre>
     * CREATE TABLE IF NOT EXISTS `tb_account`
     * (
     *     `id`        INTEGER PRIMARY KEY auto_increment,
     *     `user_name` VARCHAR(100),
     *     `age`       Integer,
     *     `birthday`  DATETIME
     * );
     * </pre>
     *
     * @param schemaSql Sql语句
     * @return
     */
    @Override
    public boolean executeSchemaSql(String schemaSql) {
        try (Connection connection = h2DruidDataSource.getConnection(); Statement statement = connection.createStatement()) {
            // 执行语句
            statement.execute(schemaSql);
        } catch (Exception e) {
            Log.error(String.format("【执行sql语句异常】schemeSql：%s，异常：%s", schemaSql, e.getMessage()), e);
            return false;
        }
        return true;
    }

    /**
     * 创建scheme数据表
     *
     * @param tableInfo 数据表信息
     * @return
     */
    @Override
    public boolean createSchemaTable(TableInfo tableInfo) {
        if (null != tableInfo) {
            // 数据表
            String tableName = tableInfo.getTableName();
            // 数据列
            List<ColumnInfo> columns = tableInfo.getColumns();
            // 构建建表语句
            StringBuilder sql = new StringBuilder();
            sql.append(String.format("CREATE TABLE IF NOT EXISTS `%s`", tableName));
            sql.append("(");
            for (int i = 0; i < columns.size(); i++) {
                ColumnInfo column = columns.get(i);
                if (Boolean.TRUE.equals(column.getIsPrimary())) {
                    switch (column.getColumnType()) {
                        case INTEGER:
                        case LONG:
                            sql.append(String.format("\n`%s`\t%s PRIMARY KEY auto_increment", column.getColumnName(), column.getColumnType().getH2JdbcType()));
                            break;
                        default:
                            sql.append(String.format("\n`%s`\t%s PRIMARY KEY", column.getColumnName(), column.getColumnType().getH2JdbcType()));
                    }
                } else {
                    sql.append(String.format("\n`%s`\t%s", column.getColumnName(), column.getColumnType().getH2JdbcType()));
                }
                if (i != columns.size() - 1) {
                    sql.append(",");
                }
            }
            sql.append("\n);");
            Log.debug(String.format("【执行建表语句】：\n%s", sql));
            return this.executeSchemaSql(sql.toString().replace("\n", "").replace("\t", ""));
        }
        return false;
    }

    /**
     * 展示schema数据表
     *
     * @param tableName 数据表名
     * @return
     */
    @Override
    public List<Row> showSchemaTable(String tableName) {
        return Db.selectListBySql(String.format("show columns from %s ;", tableName));
    }

    /**
     * 新增schema数据列
     *
     * @param tableName 数据表名称
     * @param columns   新增数据列
     * @return
     */
    @Override
    public boolean addSchemaTableColumns(String tableName, List<ColumnInfo> columns) {
        String schemaSql = "";
        try (Connection connection = h2DruidDataSource.getConnection(); Statement statement = connection.createStatement()) {
            for (ColumnInfo column : columns) {
                schemaSql = String.format("ALTER TABLE IF EXISTS %s ADD IF NOT EXISTS %s %s ;", tableName, column.getColumnName(), column.getColumnType().getH2JdbcType());
                // 执行语句
                statement.execute(schemaSql);
            }
        } catch (Exception e) {
            Log.error(String.format("【执行sql语句异常】schemeSql：%s，异常：%s", schemaSql, e.getMessage()), e);
            return false;
        }
        return true;
    }

    /**
     * 保存schema数据表
     *
     * @param tableInfo 数据表
     * @return
     */
    @Override
    public boolean saveSchemaTable(TableInfo tableInfo) {
        // 查询数据表结构
        List<Row> rows = this.showSchemaTable(tableInfo.getTableName());
        if (null == rows || rows.size() == 0) {
            // 创建数据表结构
            return this.createSchemaTable(tableInfo);
        } else {
            Map<String, Row> rowMapping = Maps.newHashMap();
            for (Row row : rows) {
                rowMapping.put(row.getString("FIELD"), row);
            }
            List<ColumnInfo> columns = tableInfo.getColumns();
            List<ColumnInfo> addColumns = new ArrayList<>();
            for (ColumnInfo column : columns) {
                // 是否已定义字段
                Row row = rowMapping.get(column.getColumnName());
                if (null == row) {
                    addColumns.add(column);
                }
            }
            // 新增额外数据列
            return this.addSchemaTableColumns(tableInfo.getTableName(), addColumns);
        }
    }

    /**
     * 新增数据
     *
     * @param tableInfo 数据表
     * @param rowObj    行数据
     * @return
     */
    @Override
    public Integer insertJsonObjectRow(TableInfo tableInfo, JsonObject rowObj) {
        Map<String, ColumnInfo> columnTypeMapping = this.buildColumnTypeMapping(tableInfo.getColumns());
        Row row = this.buildRow(rowObj, columnTypeMapping);
        if (row.size() > 0) {
            return Db.insert(tableInfo.getTableName(), row);
        }
        return 0;
    }

    /**
     * 构建数据行
     *
     * @param rowObj            数据行
     * @param columnTypeMapping 字段映射
     * @return
     */
    private Row buildRow(JsonObject rowObj, Map<String, ColumnInfo> columnTypeMapping) {
        // 不启用临时会话字段
        return buildRow(rowObj, null, null, columnTypeMapping);
    }

    /**
     * 构建数据行
     *
     * @param rowObj            数据行
     * @param tableInfo         数据表
     * @param sessionId         会话ID
     * @param columnTypeMapping 字段映射
     * @return
     */
    private Row buildRow(JsonObject rowObj, TableInfo tableInfo, String sessionId, Map<String, ColumnInfo> columnTypeMapping) {
        Row row = new Row();
        for (Map.Entry<String, JsonElement> entry : rowObj.entrySet()) {
            String key = entry.getKey();
            ColumnInfo columnInfo = columnTypeMapping.get(key);
            if (null == columnInfo) {
                continue;
            }
            String fieldName = columnInfo.getColumnName();
            JsonElement value = entry.getValue();
            // ------------------------------- 补充临时会话字段信息 --------------------------------------------
            if (StringUtils.isNotEmpty(sessionId)) {
                // 获取会话字段名称信息
                String sessionIdName = tableInfo.getSessionId();
                String sessionKeyName = tableInfo.getSessionKey();
                // 添加会话ID
                row.set(sessionIdName, sessionId);
                // 优先获取主键字段，若无主键字段，则使用主键别名
                String primaryId = tableInfo.getPrimaryId();
                JsonElement idObj = rowObj.get(primaryId);
                if (null == idObj || idObj.isJsonNull()) {
                    // 主键别名
                    String primaryAlias = tableInfo.getPrimaryAlias();
                    String id = rowObj.get(primaryAlias).getAsString();
                    row.set(sessionKeyName, String.format("%s|%s", id, sessionId));
                } else {
                    if (idObj.isJsonObject()) {
                        // 若主键是原始objectId
                        JsonObject obj = idObj.getAsJsonObject();
                        row.set(sessionKeyName, String.format("%s|%s", obj.get("$oid").getAsString(), sessionId));
                    } else {
                        String id = idObj.getAsString();
                        row.set(sessionKeyName, String.format("%s|%s", id, sessionId));
                    }
                }
            }
            // --------------------------------------------------------------------------------------------
            try {
                if (null == value || value.isJsonNull()) {
                    row.set(fieldName, null);
                } else {
                    switch (columnInfo.getColumnType()) {
                        case DOUBLE:
                            row.set(fieldName, value.getAsDouble());
                            break;
                        case FLOAT:
                            row.set(fieldName, value.getAsFloat());
                            break;
                        case INTEGER:
                            row.set(fieldName, value.getAsInt());
                            break;
                        case LONG:
                            row.set(fieldName, value.getAsLong());
                            break;
                        case NUMBER:
                            row.set(fieldName, value.getAsBigDecimal());
                            break;
                        case BOOLEAN:
                            row.set(fieldName, value.getAsBoolean());
                        case STRING:
                        case TEXT:
                        case DATE:
                        case DATETIME:
                            // 若数据类型错误，直接跳过
                            if (value instanceof JsonArray) {
                                continue;
                            }
                            if (value instanceof JsonObject) {
                                if (columnInfo.getIsPrimary()) {
                                    // 若主键是原始objectId
                                    JsonObject obj = value.getAsJsonObject();
                                    row.set(fieldName, obj.get("$oid").getAsString());
                                }
                            } else {
                                row.set(fieldName, value.getAsString());
                            }
                            break;
                        default:
                    }
                }
            } catch (Exception e) {
                Log.error(String.format("原始数据异常：%s", value));
                throw new RuntimeException(e);
            }
        }
        return row;
    }

    /**
     * 构建数据列映射
     *
     * @param columns 数据列
     * @return
     */
    public Map<String, ColumnInfo> buildColumnTypeMapping(List<ColumnInfo> columns) {
        Map<String, ColumnInfo> columnTypeMapping = Maps.newHashMap();
        for (ColumnInfo column : columns) {
            // 实体类别名字段映射，解决mongodb主键_id与实体类id不一致问题
            if (StringUtils.isNotEmpty(column.getColumnAlias())) {
                columnTypeMapping.put(column.getColumnAlias(), column);
            }
            // 数据库列字段映射
            columnTypeMapping.put(column.getColumnName(), column);
        }
        return columnTypeMapping;
    }

    /**
     * 批量新增数据
     * <p>
     * 说明：默认实时新建数据表，每次提交1000条数据
     *
     * @param tableInfo 数据表
     * @param rowsObj   行数据集
     * @return
     */
    @Override
    public Integer batchInsertJsonObjectRow(TableInfo tableInfo, List<JsonObject> rowsObj) {
        return this.batchInsertJsonObjectRow(tableInfo, rowsObj, true, 1000);
    }

    /**
     * 批量新增数据
     *
     * @param tableInfo         数据表
     * @param rowsObj           行数据集
     * @param createSchemaTable 创建数据表，说明：每次调用都会比对数据表字段是否存在，若不存在则新增数据列，而已存在的数据列，即使类型变更也不好修改。
     * @param batchSize         每次提交的数据量
     * @return
     */
    @Override
    public Integer batchInsertJsonObjectRow(TableInfo tableInfo, List<JsonObject> rowsObj, Boolean createSchemaTable, Integer batchSize) {
        if (Boolean.TRUE.equals(createSchemaTable)) {
            // 创建表格schema
            boolean successCreateSchemaTable = this.createSchemaTable(tableInfo);
            if (!successCreateSchemaTable) {
                return 0;
            }
        }
        // 构建字段映射
        Map<String, ColumnInfo> columnTypeMapping = this.buildColumnTypeMapping(tableInfo.getColumns());
        List<Row> rows = new ArrayList<>();
        for (JsonObject rowObj : rowsObj) {
            Row row = this.buildRow(rowObj, columnTypeMapping);
            if (row.size() > 0) {
                rows.add(row);
            }
        }
        // 新增数据
        int[] insertBatch = Db.insertBatch(tableInfo.getTableName(), rows, Optional.ofNullable(batchSize).orElse(1000));
        int sum = 0;
        for (int saveNum : insertBatch) {
            sum = sum + saveNum;
        }
        return sum;
    }

    /**
     * 批量新增数据
     *
     * @param tableInfo         数据表
     * @param jsonArray         行数据集
     * @param createSchemaTable 创建数据表，说明：每次调用都会比对数据表字段是否存在，若不存在则新增数据列，而已存在的数据列，即使类型变更也不好修改。
     * @param batchSize         每次提交的数据量
     * @return
     */
    @Override
    public Integer batchInsertJsonObjectRow(TableInfo tableInfo, JsonArray jsonArray, Boolean createSchemaTable, Integer batchSize) {
        if (Boolean.TRUE.equals(createSchemaTable)) {
            // 创建表格schema
            boolean successCreateSchemaTable = this.createSchemaTable(tableInfo);
            if (!successCreateSchemaTable) {
                return 0;
            }
        }
        // 构建字段映射
        Map<String, ColumnInfo> columnTypeMapping = this.buildColumnTypeMapping(tableInfo.getColumns());
        List<Row> rows = new ArrayList<>();
        for (JsonElement jsonElement : jsonArray) {
            JsonObject rowObj = jsonElement.getAsJsonObject();
            Row row = this.buildRow(rowObj, columnTypeMapping);
            if (row.size() > 0) {
                rows.add(row);
            }
        }
        // 新增数据
        int[] insertBatch = Db.insertBatch(tableInfo.getTableName(), rows, Optional.ofNullable(batchSize).orElse(1000));
        int sum = 0;
        for (int saveNum : insertBatch) {
            sum = sum + saveNum;
        }
        return sum;
    }

    /**
     * 批量新增数据
     *
     * @param tableInfo         数据表
     * @param sessionId         会话ID，仅对开启enableSession生效。
     * @param inputArray        行数据集
     * @param createSchemaTable 创建数据表，说明：每次调用都会比对数据表字段是否存在，若不存在则新增数据列，而已存在的数据列，即使类型变更也不好修改。
     * @param batchSize         每次提交的数据量
     * @return
     */
    @Override
    public Integer batchInsertJsonObjectRowBySession(TableInfo tableInfo, String sessionId, JsonArray inputArray, Boolean createSchemaTable, Integer batchSize) {
        if (Boolean.TRUE.equals(createSchemaTable)) {
            // 创建表格schema
            boolean successCreateSchemaTable = this.createSchemaTable(tableInfo);
            if (!successCreateSchemaTable) {
                return 0;
            }
        }
        // 构建字段映射
        Map<String, ColumnInfo> columnTypeMapping = this.buildColumnTypeMapping(tableInfo.getColumns());
        List<Row> rows = new ArrayList<>();
        for (JsonElement jsonElement : inputArray) {
            JsonObject rowObj = jsonElement.getAsJsonObject();
            // 构建数据行
            Row row = this.buildRow(rowObj, tableInfo, sessionId, columnTypeMapping);
            if (row.size() > 0) {
                rows.add(row);
            }
        }
        // 新增数据
        int[] insertBatch = Db.insertBatch(tableInfo.getTableName(), rows, Optional.ofNullable(batchSize).orElse(1000));
        int sum = 0;
        for (int saveNum : insertBatch) {
            sum = sum + saveNum;
        }
        return sum;
    }

    /**
     * 批量新增数据
     *
     * @param tableInfo         数据表
     * @param sessionId         会话ID，仅对开启enableSession生效。
     * @param inputArray        行数据集
     * @param createSchemaTable 创建数据表，说明：每次调用都会比对数据表字段是否存在，若不存在则新增数据列，而已存在的数据列，即使类型变更也不好修改。
     * @param batchSize         每次提交的数据量
     * @return
     */
    @Override
    public Integer batchInsertJsonObjectRowBySession(TableInfo tableInfo, String sessionId, List<JsonObject> inputArray, Boolean createSchemaTable, Integer batchSize) {
        if (Boolean.TRUE.equals(createSchemaTable)) {
            // 创建表格schema
            boolean successCreateSchemaTable = this.createSchemaTable(tableInfo);
            if (!successCreateSchemaTable) {
                return 0;
            }
        }
        // 构建字段映射
        Map<String, ColumnInfo> columnTypeMapping = this.buildColumnTypeMapping(tableInfo.getColumns());
        List<Row> rows = new ArrayList<>();
        for (JsonObject rowObj : inputArray) {
            // 构建数据行
            Row row = this.buildRow(rowObj, tableInfo, sessionId, columnTypeMapping);
            if (row.size() > 0) {
                rows.add(row);
            }
        }
        // 新增数据
        int[] insertBatch = Db.insertBatch(tableInfo.getTableName(), rows, Optional.ofNullable(batchSize).orElse(1000));
        int sum = 0;
        for (int saveNum : insertBatch) {
            sum = sum + saveNum;
        }
        return sum;
    }

    /**
     * 批量新增数据
     *
     * @param schemaClazz       数据表实体类
     * @param rowsObj           行数据集
     * @param createSchemaTable 创建数据表，说明：每次调用都会比对数据表字段是否存在，若不存在则新增数据列，而已存在的数据列，即使类型变更也不好修改。
     * @param batchSize         每次提交的数据量
     * @return
     */
    @Override
    public <T_Schema> Integer batchInsertJsonObjectRow(Class<T_Schema> schemaClazz, List<JsonObject> rowsObj, Boolean createSchemaTable, Integer batchSize) {
        TableInfo tableInfo = this.clazzToTableInfo(schemaClazz);
        if (Boolean.TRUE.equals(createSchemaTable)) {
            // 创建表格schema
            boolean successCreateSchemaTable = this.createSchemaTable(tableInfo);
            if (!successCreateSchemaTable) {
                return 0;
            }
        }
        // 构建字段映射
        Map<String, ColumnInfo> columnTypeMapping = this.buildColumnTypeMapping(tableInfo.getColumns());
        List<Row> rows = new ArrayList<>();
        for (JsonObject rowObj : rowsObj) {
            Row row = this.buildRow(rowObj, columnTypeMapping);
            if (row.size() > 0) {
                rows.add(row);
            }
        }
        // 新增数据
        int[] insertBatch = Db.insertBatch(tableInfo.getTableName(), rows, Optional.ofNullable(batchSize).orElse(1000));
        int sum = 0;
        for (int saveNum : insertBatch) {
            sum = sum + saveNum;
        }
        return sum;
    }

    /**
     * 批量新增数据
     *
     * @param schemaClazz       数据表实体类
     * @param jsonArray         行数据集
     * @param createSchemaTable 创建数据表，说明：每次调用都会比对数据表字段是否存在，若不存在则新增数据列，而已存在的数据列，即使类型变更也不好修改。
     * @param batchSize         每次提交的数据量
     * @return
     */
    @Override
    public <T_Schema> Integer batchInsertJsonObjectRow(Class<T_Schema> schemaClazz, JsonArray jsonArray, Boolean createSchemaTable, Integer batchSize) {
        TableInfo tableInfo = this.clazzToTableInfo(schemaClazz);
        if (Boolean.TRUE.equals(createSchemaTable)) {
            // 创建表格schema
            boolean successCreateSchemaTable = this.createSchemaTable(tableInfo);
            if (!successCreateSchemaTable) {
                return 0;
            }
        }
        // 构建字段映射
        Map<String, ColumnInfo> columnTypeMapping = this.buildColumnTypeMapping(tableInfo.getColumns());
        List<Row> rows = new ArrayList<>();
        for (JsonElement jsonElement : jsonArray) {
            JsonObject rowObj = jsonElement.getAsJsonObject();
            Row row = this.buildRow(rowObj, columnTypeMapping);
            if (row.size() > 0) {
                rows.add(row);
            }
        }
        // 新增数据
        int[] insertBatch = Db.insertBatch(tableInfo.getTableName(), rows, Optional.ofNullable(batchSize).orElse(1000));
        int sum = 0;
        for (int saveNum : insertBatch) {
            sum = sum + saveNum;
        }
        return sum;
    }

    /**
     * 查询数据集
     *
     * @param tableInfo         数据表
     * @param createSchemaTable 创建数据表，说明：每次调用都会比对数据表字段是否存在，若不存在则新增数据列，而已存在的数据列，即使类型变更也不好修改。
     * @param queryWrapper      查询条件
     * @return
     */
    @Override
    public List<JsonObject> queryJsonObjectList(TableInfo tableInfo, Boolean createSchemaTable, QueryWrapper queryWrapper) {
        if (Boolean.TRUE.equals(createSchemaTable)) {
            // 创建数据表
            this.createSchemaTable(tableInfo);
        }
        // 数据查询
        List<Row> rows = Db.selectListByQuery(tableInfo.getTableName(), queryWrapper);
        Map<String, ColumnInfo> columnTypeEnumMapping = this.buildColumnTypeMapping(tableInfo.getColumns());
        List<JsonObject> rowsObj = new ArrayList<>();
        if (null != rows && rows.size() > 0) {
            for (Row row : rows) {
                Set<String> keySet = row.keySet();
                JsonObject rowObj = new JsonObject();
                for (String key : keySet) {
                    ColumnInfo columnInfo = columnTypeEnumMapping.get(key);
                    ColumnTypeEnum columnTypeEnum = columnInfo.getColumnType();
                    if (null == columnTypeEnum) {
                        continue;
                    }
                    // 数据类型处理
                    switch (columnTypeEnum) {
                        case DOUBLE:
                            rowObj.addProperty(key, row.getDouble(key));
                            break;
                        case FLOAT:
                            rowObj.addProperty(key, row.getFloat(key));
                            break;
                        case INTEGER:
                            rowObj.addProperty(key, row.getInt(key));
                            break;
                        case LONG:
                            rowObj.addProperty(key, row.getLong(key));
                            break;
                        case NUMBER:
                            rowObj.addProperty(key, row.getBigDecimal(key));
                            break;
                        case BOOLEAN:
                            rowObj.addProperty(key, row.getBoolean(key));
                        case STRING:
                        case TEXT:
                        case DATE:
                        case DATETIME:
                            rowObj.addProperty(key, row.getString(key));
                            break;
                        default:
                    }
                    rowsObj.add(rowObj);
                }
            }
            return rowsObj;
        }
        return Lists.newArrayList();
    }

    /**
     * 查询数据集
     * <p>
     * 说明：默认实时创建或更新数据表结构
     *
     * @param tableInfo    数据表
     * @param queryWrapper 查询条件
     * @return
     */
    @Override
    public List<JsonObject> queryJsonObjectList(TableInfo tableInfo, QueryWrapper queryWrapper) {
        return this.queryJsonObjectList(tableInfo, true, queryWrapper);
    }

    /**
     * 查询数据集合
     *
     * @param schemaClazz       数据表实体类
     * @param modelClazz        返回值实体类
     * @param createSchemaTable 创建数据表，说明：每次调用都会比对数据表字段是否存在，若不存在则新增数据列，而已存在的数据列，即使类型变更也不好修改。
     * @param sql               查询sql语句
     * @param args              参数
     * @return
     */
    @Override
    public <M_Schema, T_Model> List<T_Model> queryDataListBySql(Class<M_Schema> schemaClazz, Class<T_Model> modelClazz, Boolean createSchemaTable, String sql, Object... args) {
        if (Boolean.TRUE.equals(createSchemaTable)) {
            // 实体类转换
            TableInfo tableInfo = this.clazzToTableInfo(schemaClazz);
            // 创建数据表
            this.createSchemaTable(tableInfo);
        }
        // 查询数据
        return this.queryListBySql(modelClazz, sql, args);
    }

    /**
     * 查询并分析会话数据集合，主要用于数据统计
     * <p>
     * 说明：新增数据后，再统计数据，最后删除临时数据
     *
     * @param schemaClazz       数据表实体类
     * @param modelClazz        返回值实体类
     * @param createSchemaTable 创建数据表，说明：每次调用都会比对数据表字段是否存在，若不存在则新增数据列，而已存在的数据列，即使类型变更也不会修改。
     * @param sessionId         会话ID，说明：仅对开启enableSession生效
     * @param inputArray        输入行数据集合
     * @param sql               查询sql语句
     * @param args              sql参数变量
     * @param <M_Schema>        数据表泛型
     * @param <T_Model>         返回值泛型
     * @return
     */
    @Override
    public <M_Schema, T_Model> List<T_Model> analysisSessionDataListBySql(Class<M_Schema> schemaClazz, Class<T_Model> modelClazz, Boolean createSchemaTable,
                                                                          String sessionId, JsonArray inputArray,
                                                                          String sql, Object... args) {
        // 默认删除会话数据
        return this.analysisSessionDataListBySql(schemaClazz, modelClazz, createSchemaTable, sessionId, inputArray, true, sql, args);
    }

    /**
     * 查询并分析会话数据集合，主要用于数据统计
     * <p>
     * 说明：新增数据后，再统计数据，最后删除临时数据
     *
     * @param schemaClazz       数据表实体类
     * @param modelClazz        返回值实体类
     * @param createSchemaTable 创建数据表，说明：每次调用都会比对数据表字段是否存在，若不存在则新增数据列，而已存在的数据列，即使类型变更也不会修改。
     * @param sessionId         会话ID，说明：仅对开启enableSession生效
     * @param inputArray        输入行数据集合
     * @param sql               查询sql语句
     * @param args              sql参数变量
     * @return
     */
    @Override
    public <M_Schema, T_Model> List<T_Model> analysisSessionDataListBySql(Class<M_Schema> schemaClazz, Class<T_Model> modelClazz, Boolean createSchemaTable, String sessionId, List<JsonObject> inputArray, String sql, Object... args) {
        // 默认删除会话数据
        return this.analysisSessionDataListBySql(schemaClazz, modelClazz, createSchemaTable, sessionId, inputArray, true, sql, args);
    }

    /**
     * 查询并分析会话数据集合，主要用于数据统计
     * <p>
     * 说明：新增数据后，再统计数据，最后删除临时数据
     *
     * @param schemaClazz       数据表实体类
     * @param modelClazz        返回值实体类
     * @param createSchemaTable 创建数据表，说明：每次调用都会比对数据表字段是否存在，若不存在则新增数据列，而已存在的数据列，即使类型变更也不会修改。
     * @param sessionId         会话ID，说明：仅对开启enableSession生效
     * @param inputArray        输入行数据集合
     * @param deleteSessionData 执行结束是否删除会话数据
     * @param sql               查询sql语句
     * @param args              sql参数变量
     * @return
     */
    @Override
    public <M_Schema, T_Model> List<T_Model> analysisSessionDataListBySql(Class<M_Schema> schemaClazz, Class<T_Model> modelClazz, Boolean createSchemaTable, String sessionId, JsonArray inputArray, Boolean deleteSessionData, String sql, Object... args) {
        // 实体类转换
        TableInfo tableInfo = this.clazzToTableInfo(schemaClazz);
        try {
            // 保存待分析的行数据集合
            this.batchInsertJsonObjectRowBySession(tableInfo, sessionId, inputArray, createSchemaTable, 10000);
            // 查询数据
            return this.queryListBySql(modelClazz, sql, args);
        } finally {
            if (Boolean.TRUE.equals(deleteSessionData)) {
                // 数据分析完后，删除临时数据
                this.deleteTempSessionData(tableInfo, sessionId);
            }
        }
    }

    /**
     * 查询并分析会话数据集合，主要用于数据统计
     * <p>
     * 说明：新增数据后，再统计数据，最后删除临时数据
     *
     * @param schemaClazz       数据表实体类
     * @param modelClazz        返回值实体类
     * @param createSchemaTable 创建数据表，说明：每次调用都会比对数据表字段是否存在，若不存在则新增数据列，而已存在的数据列，即使类型变更也不会修改。
     * @param sessionId         会话ID，说明：仅对开启enableSession生效
     * @param inputArray        输入行数据集合
     * @param deleteSessionData 执行结束是否删除会话数据
     * @param sql               查询sql语句
     * @param args              sql参数变量
     * @return
     */
    @Override
    public <M_Schema, T_Model> List<T_Model> analysisSessionDataListBySql(Class<M_Schema> schemaClazz, Class<T_Model> modelClazz, Boolean createSchemaTable, String sessionId, List<JsonObject> inputArray, Boolean deleteSessionData, String sql, Object... args) {
        // 实体类转换
        TableInfo tableInfo = this.clazzToTableInfo(schemaClazz);
        try {
            // 保存待分析的行数据集合
            this.batchInsertJsonObjectRowBySession(tableInfo, sessionId, inputArray, createSchemaTable, 10000);
            // 查询数据
            return this.queryListBySql(modelClazz, sql, args);
        } finally {
            if (Boolean.TRUE.equals(deleteSessionData)) {
                // 数据分析完后，删除临时数据
                this.deleteTempSessionData(tableInfo, sessionId);
            }
        }
    }

    /**
     * 查询数据集合
     *
     * @param modelClazz 实体类
     * @param sql        查询语句
     * @param args       参数
     * @param <T_Model>  实体模型
     * @return
     */
    @Override
    public <T_Model> List<T_Model> queryListBySql(Class<T_Model> modelClazz, String sql, Object... args) {
        // 查询数据
        List<Row> rows = Db.selectListBySql(sql, args);
        List<T_Model> list = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(rows)) {
            for (Row row : rows) {
                // 实体类转换，若数据列与实体类不一致，使用@SerializedName("_id")注解解决。
                T_Model tModel = GSON.fromJson(GSON.toJson(row), modelClazz);
                list.add(tModel);
            }
            return list;
        }
        return Lists.newArrayList();
    }

    /**
     * 删除会话数据
     *
     * @param tableInfo 数据表
     * @param sessionId 会话ID
     */
    @Override
    public void deleteTempSessionData(TableInfo tableInfo, String sessionId) {
        // 数据分析完后，删除临时数据
        if (StringUtils.isNotEmpty(sessionId)) {
            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.where(String.format("`%s` = ?", tableInfo.getSessionId()), sessionId);
            // 删除临时会话数据
            Db.deleteByQuery(tableInfo.getTableName(), queryWrapper);
        }
    }

    /**
     * 需要排除的数据字段
     */
    private static final String EXCLUDE_FIELD = "serialVersionUID";

    /**
     * 实体类转换数据表schema
     * <p>
     * 说明：实体类必须添加，@ColumnAnno与@TableAnno注解
     *
     * @param schemaClazz 数据表实体类
     * @param <T_Schema>  数据表泛型
     * @return
     */
    @Override
    @SuppressWarnings("all")
    public <T_Schema> TableInfo clazzToTableInfo(Class<T_Schema> schemaClazz) {
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
                    // 数据列字段定义
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

    /**
     * 缓存创建的数据表
     */
    private static final Map<String, TableInfo> CACHE_CREATE_TABLE = new ConcurrentHashMap<>();

    /**
     * 创建schema数据表
     *
     * @param schemaClazz 数据表实体类
     * @return
     */
    @Override
    public <T_Schema> boolean createSchemaTable(Class<T_Schema> schemaClazz) {
        String typeName = schemaClazz.getName();
        // 判断数据是否创建
        TableInfo tableInfoCache = CACHE_CREATE_TABLE.get(typeName);
        if (null != tableInfoCache) {
            return true;
        } else {
            // 获取数据表信息
            TableInfo tableInfo = this.clazzToTableInfo(schemaClazz);
            // 创建数据表
            boolean schemaTable = this.createSchemaTable(tableInfo);
            if (schemaTable) {
                // 数据表创建成功，则缓存记录，不在重复创建
                CACHE_CREATE_TABLE.put(typeName, tableInfo);
            }
            return schemaTable;
        }
    }

}
