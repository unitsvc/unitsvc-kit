package com.unitsvc.kit.facade.datasource.h2;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.unitsvc.kit.facade.datasource.table.ColumnInfo;
import com.unitsvc.kit.facade.datasource.table.TableInfo;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.row.Row;

import java.util.List;

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
 * @since : 2023/6/20 10:07
 **/
public interface H2DataSourceService {

    /**
     * 执行schemaSql语句
     *
     * @param schemaSql Sql语句
     * @return
     */
    boolean executeSchemaSql(String schemaSql);

    /**
     * 创建schema数据表
     *
     * @param tableInfo 数据表信息
     * @return
     */
    boolean createSchemaTable(TableInfo tableInfo);

    /**
     * 展示schema数据表
     *
     * @param tableName 数据表名
     * @return
     */
    List<Row> showSchemaTable(String tableName);

    /**
     * 新增schema数据列
     *
     * @param tableName 数据表名称
     * @param columns   新增数据列
     * @return
     */
    boolean addSchemaTableColumns(String tableName, List<ColumnInfo> columns);

    /**
     * 保存schema数据表
     *
     * @param tableInfo 数据表
     * @return
     */
    boolean saveSchemaTable(TableInfo tableInfo);

    /**
     * 新增单条数据
     *
     * @param tableInfo 数据表
     * @param rowObj    行数据
     * @return
     */
    Integer insertJsonObjectRow(TableInfo tableInfo, JsonObject rowObj);

    /**
     * 批量新增数据
     * <p>
     * 说明：默认实时新建数据表，每次提交1000条数据
     *
     * @param tableInfo 数据表
     * @param rowsObj   行数据集
     * @return
     */
    Integer batchInsertJsonObjectRow(TableInfo tableInfo, List<JsonObject> rowsObj);

    /**
     * 批量新增数据
     *
     * @param tableInfo         数据表
     * @param rowsObj           行数据集
     * @param createSchemaTable 创建数据表，说明：每次调用都会比对数据表字段是否存在，若不存在则新增数据列，而已存在的数据列，即使类型变更也不好修改。
     * @param batchSize         每次提交的数据量
     * @return
     */
    Integer batchInsertJsonObjectRow(TableInfo tableInfo, List<JsonObject> rowsObj, Boolean createSchemaTable, Integer batchSize);

    /**
     * 批量新增数据
     *
     * @param tableInfo         数据表
     * @param jsonArray         行数据集
     * @param createSchemaTable 创建数据表，说明：每次调用都会比对数据表字段是否存在，若不存在则新增数据列，而已存在的数据列，即使类型变更也不好修改。
     * @param batchSize         每次提交的数据量
     * @return
     */
    Integer batchInsertJsonObjectRow(TableInfo tableInfo, JsonArray jsonArray, Boolean createSchemaTable, Integer batchSize);

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
    Integer batchInsertJsonObjectRowBySession(TableInfo tableInfo, String sessionId, JsonArray inputArray, Boolean createSchemaTable, Integer batchSize);

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
    Integer batchInsertJsonObjectRowBySession(TableInfo tableInfo, String sessionId, List<JsonObject> inputArray, Boolean createSchemaTable, Integer batchSize);

    /**
     * 批量新增数据
     *
     * @param schemaClazz       数据表实体类
     * @param rowsObj           行数据集
     * @param createSchemaTable 创建数据表，说明：每次调用都会比对数据表字段是否存在，若不存在则新增数据列，而已存在的数据列，即使类型变更也不好修改。
     * @param batchSize         每次提交的数据量
     * @param <T_Schema>
     * @return
     */
    <T_Schema> Integer batchInsertJsonObjectRow(Class<T_Schema> schemaClazz, List<JsonObject> rowsObj, Boolean createSchemaTable, Integer batchSize);

    /**
     * 批量新增数据
     *
     * @param schemaClazz       数据表实体类
     * @param jsonArray         行数据集
     * @param createSchemaTable 创建数据表，说明：每次调用都会比对数据表字段是否存在，若不存在则新增数据列，而已存在的数据列，即使类型变更也不好修改。
     * @param batchSize         每次提交的数据量
     * @param <T_Schema>
     * @return
     */
    <T_Schema> Integer batchInsertJsonObjectRow(Class<T_Schema> schemaClazz, JsonArray jsonArray, Boolean createSchemaTable, Integer batchSize);

    /**
     * 查询数据集
     *
     * @param tableInfo         数据表
     * @param createSchemaTable 创建数据表，说明：每次调用都会比对数据表字段是否存在，若不存在则新增数据列，而已存在的数据列，即使类型变更也不好修改。
     * @param queryWrapper      查询条件
     * @return
     */
    List<JsonObject> queryJsonObjectList(TableInfo tableInfo, Boolean createSchemaTable, QueryWrapper queryWrapper);

    /**
     * 查询数据集
     * <p>
     * 说明：默认实时创建或更新数据表结构
     *
     * @param tableInfo    数据表
     * @param queryWrapper 查询条件
     * @return
     */
    List<JsonObject> queryJsonObjectList(TableInfo tableInfo, QueryWrapper queryWrapper);

    /**
     * 查询数据集合
     *
     * @param schemaClazz       数据表实体类
     * @param modelClazz        返回值实体类
     * @param createSchemaTable 创建数据表，说明：每次调用都会比对数据表字段是否存在，若不存在则新增数据列，而已存在的数据列，即使类型变更也不好修改。
     * @param sql               查询sql语句
     * @param args              参数
     * @param <M_Schema>        数据表泛型
     * @param <T_Model>         返回值泛型
     * @return
     */
    <M_Schema, T_Model> List<T_Model> queryDataListBySql(Class<M_Schema> schemaClazz, Class<T_Model> modelClazz, Boolean createSchemaTable, String sql, Object... args);

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
    <M_Schema, T_Model> List<T_Model> analysisSessionDataListBySql(Class<M_Schema> schemaClazz, Class<T_Model> modelClazz, Boolean createSchemaTable,
                                                                   String sessionId, JsonArray inputArray,
                                                                   String sql, Object... args);

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
    <M_Schema, T_Model> List<T_Model> analysisSessionDataListBySql(Class<M_Schema> schemaClazz, Class<T_Model> modelClazz, Boolean createSchemaTable,
                                                                   String sessionId, List<JsonObject> inputArray,
                                                                   String sql, Object... args);

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
     * @param <M_Schema>        数据表泛型
     * @param <T_Model>         返回值泛型
     * @return
     */
    <M_Schema, T_Model> List<T_Model> analysisSessionDataListBySql(Class<M_Schema> schemaClazz, Class<T_Model> modelClazz, Boolean createSchemaTable,
                                                                   String sessionId, JsonArray inputArray, Boolean deleteSessionData,
                                                                   String sql, Object... args);

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
     * @param <M_Schema>        数据表泛型
     * @param <T_Model>         返回值泛型
     * @return
     */
    <M_Schema, T_Model> List<T_Model> analysisSessionDataListBySql(Class<M_Schema> schemaClazz, Class<T_Model> modelClazz, Boolean createSchemaTable,
                                                                   String sessionId, List<JsonObject> inputArray, Boolean deleteSessionData,
                                                                   String sql, Object... args);

    /**
     * 查询数据集合
     *
     * @param modelClazz 实体类
     * @param sql        查询语句
     * @param args       参数
     * @param <T_Model>  实体模型
     * @return
     */
    <T_Model> List<T_Model> queryListBySql(Class<T_Model> modelClazz, String sql, Object... args);

    /**
     * 删除会话数据
     *
     * @param tableInfo 数据表
     * @param sessionId 会话ID
     */
    void deleteTempSessionData(TableInfo tableInfo, String sessionId);

    /**
     * 实体类转换数据表schema
     * <p>
     * 说明：实体类必须添加，@ColumnAnno与@TableAnno注解
     *
     * @param schemaClazz 数据表实体类
     * @param <T_Schema>  数据表泛型
     * @return
     */
    <T_Schema> TableInfo clazzToTableInfo(Class<T_Schema> schemaClazz);

    /**
     * 创建schema数据表
     *
     * @param schemaClazz 数据表实体类
     * @return
     */
    <T_Schema> boolean createSchemaTable(Class<T_Schema> schemaClazz);
}
