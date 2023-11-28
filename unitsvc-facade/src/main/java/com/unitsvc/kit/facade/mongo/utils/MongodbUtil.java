package com.unitsvc.kit.facade.mongo.utils;

import com.jpardus.db.jmongo.Index;
import com.jpardus.db.jmongo.MongoIndexer;
import com.jpardus.db.jmongo.MongoQuerier;
import com.jpardus.db.jmongo.QueryCondition;
import com.jpardus.spider.facade.bean.PagedList;
import com.jpardus.spider.sccs.Log;
import com.mongodb.client.model.IndexOptions;
import org.apache.commons.lang.StringUtils;
import org.bson.BsonDocument;
import org.bson.Document;
import org.bson.codecs.BsonValueCodecProvider;
import org.bson.codecs.DocumentCodecProvider;
import org.bson.codecs.ValueCodecProvider;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.conversions.Bson;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * 功能描述：Mongodb常用方法工具类
 *
 * @author : jun
 * @version : v1.0.0
 * @since : 2023/4/13 10:21
 **/
public class MongodbUtil {

    /**
     * mongodb分页操作
     * <p>
     * 示例：
     * <pre>
     * QueryCondition queryCondition =new QueryCondition();
     * queryCondition.setFilter();
     * queryCondition.setSort();
     * queryCondition.setLimitAndPage(pageSize, pageNum);
     * queryCondition.setProjection();
     * </pre>
     *
     * @param modelClazz    模型对象类
     * @param pageCondition 查询条件
     * @return
     */
    public static <MongodbModel> PagedList<MongodbModel> mongodbPage(Class<MongodbModel> modelClazz, QueryCondition pageCondition) {
        // 获取总数
        long totalCont = MongoQuerier.count(modelClazz, pageCondition.getFilter());
        if (totalCont == 0) {
            return new PagedList<>(0, Collections.emptyList());
        }
        // 分页记录
        List<MongodbModel> modelList = Optional.ofNullable(MongoQuerier.query(modelClazz, pageCondition)).orElse(Collections.emptyList());
        PagedList<MongodbModel> page = new PagedList<>();
        page.setItems(modelList);
        page.setTotal((int) totalCont);
        return page;
    }

    /**
     * mongodb查询；Bson过滤条件转filter
     *
     * @param filter Bson格式过滤条件，由Filters构造
     * @return
     */
    public static String toJsonFilter(Bson filter) {
        if (null == filter) {
            return null;
        }
        BsonDocument bsonDocument = filter.toBsonDocument(
                BsonDocument.class,
                CodecRegistries.fromProviders(new BsonValueCodecProvider(), new ValueCodecProvider(), new DocumentCodecProvider())
        );
        return bsonDocument.toJson();
    }

    /**
     * mongodb查询：Json过滤条件转Bson格式
     *
     * @param jsonFilter Json格式过滤条件
     * @return
     */
    public static Bson toBsonFilter(String jsonFilter) {
        if (StringUtils.isEmpty(jsonFilter)) {
            return null;
        }
        return Document.parse(jsonFilter);
    }

    /**
     * 创建TTL索引
     *
     * @param tableName    数据表名称
     * @param indexName    索引名称
     * @param expireSecond 过期时间
     */
    public static void createTtlIndex(String tableName, String indexName, Long expireSecond) {
        try {
            // 索引名称
            String name = String.format("%s_ttl_index", indexName);
            // 查询索引是否创建
            List<Index> indexList = MongoIndexer.listIndex(tableName);
            long count = indexList.stream().filter(v -> v.getName().equals(name)).count();
            if (count == 0) {
                IndexOptions indexOptions = new IndexOptions();
                indexOptions.unique(false);
                indexOptions.name(name);
                indexOptions.expireAfter(expireSecond, TimeUnit.SECONDS);
                Document document = new Document().append(indexName, 1);
                String index = MongoIndexer.createIndex(tableName, document, indexOptions);
                Log.debug(String.format("表【%s】创建TTL索引【%s】", tableName, index));
            }
        } catch (Exception e) {
            Log.debug(String.format("表【%s】创建TTL索引字段【%s】异常", tableName, indexName), e);
        }
    }

}
