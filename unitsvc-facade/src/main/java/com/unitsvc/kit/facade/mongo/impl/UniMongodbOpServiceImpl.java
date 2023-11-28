package com.unitsvc.kit.facade.mongo.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.jpardus.db.jmongo.MongoCollectionName;
import com.jpardus.db.jmongo.MongoExecutor;
import com.jpardus.db.jmongo.MongoId;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.DistinctIterable;
import com.mongodb.client.MongoCursor;
import com.unitsvc.kit.facade.mongo.IUniMongodbOpService;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.json.JsonWriterSettings;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 功能描述：mongodb通用服务
 *
 * @author : jun
 * @version : v1.0.0
 * @date : 2022/10/27 16:20
 **/
@Service
public class UniMongodbOpServiceImpl implements IUniMongodbOpService {

    /**
     * mongodb主键
     */
    private static final String MONGODB_ID = "_id";

    /**
     * 序列化方式-null保留
     */
    private static final Gson GSON_INCLUDE_NULL = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd HH:mm:ss.SSS")
            .serializeNulls().create();

    /**
     * 序列化方式-null保留，_id转换成实体类id。仅注解生效
     */
    private static final Gson GSON_INCLUDE_NULL_TRANSFORM_ID = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd HH:mm:ss.SSS")
            .serializeNulls()
            .setFieldNamingStrategy(field -> {
                if (field.isAnnotationPresent(MongoId.class)) {
                    return MONGODB_ID;
                }
                return field.getName();
            })
            .create();

    /**
     * Document序列化方式
     */
    private static final JsonWriterSettings SETTINGS_DEFAULT = JsonWriterSettings.builder()
            .int64Converter((value, writer) -> writer.writeNumber(value.toString()))
            .build();

    /**
     * Document序列化方式，主键objectId对象的主键转字符串
     */
    private static final JsonWriterSettings SETTINGS_TRANSFORM_ID = JsonWriterSettings.builder()
            .int64Converter((value, writer) -> writer.writeNumber(value.toString()))
            .objectIdConverter((value, writer) -> writer.writeString(value.toString()))
            .build();

    /**
     * 获取指定字段的不重复值
     * <p>
     * 警告：不要对主键_id操作
     *
     * @param collectionClazz 待查询的集合实体类，备注：必须包含实体类注解
     * @param fieldName       查重字段
     * @param filter          过滤条件
     * @return
     */
    @Override
    public List<String> distinct(Class<?> collectionClazz, String fieldName, Bson filter) {
        this.checkDistinctMongodbId(fieldName);
        // 获取集合注解
        MongoCollectionName collectionName = collectionClazz.getAnnotation(MongoCollectionName.class);
        if (null == collectionName) {
            throw new RuntimeException("缺失mongodb集合实体类注解");
        }
        return this.distinct(collectionName.value(), fieldName, filter);
    }

    /**
     * 获取指定字段的不重复值
     * <p>
     * 警告：不要对主键_id操作
     *
     * @param collectionName 集合名称
     * @param fieldName      查询字段
     * @param filter         过滤条件
     * @return
     */
    @Override
    public List<String> distinct(String collectionName, String fieldName, Bson filter) {
        this.checkDistinctMongodbId(fieldName);
        return MongoExecutor.executeCollection(collectionName, (collection) -> {
            List<String> distinctList = new ArrayList<>();
            // 自动关闭游标
            try (MongoCursor<String> cursor = collection.distinct(fieldName, filter, String.class).iterator()) {
                cursor.forEachRemaining(distinctList::add);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            return distinctList;
        });
    }

    /**
     * 获取指定字段的不重复值
     * <p>
     * 警告：不要对主键_id操作
     *
     * @param collectionClazz 待查询的集合实体类，备注：必须包含实体类注解
     * @param fieldName       查重字段
     * @return
     */
    @Override
    public List<String> distinct(Class<?> collectionClazz, String fieldName) {
        this.checkDistinctMongodbId(fieldName);
        // 获取集合注解
        MongoCollectionName collectionName = collectionClazz.getAnnotation(MongoCollectionName.class);
        if (null == collectionName) {
            throw new RuntimeException("缺失mongodb集合实体类注解");
        }
        return this.distinct(collectionName.value(), fieldName);
    }

    /**
     * 获取指定字段的不重复值
     * <p>
     * 警告：不要对主键_id操作
     *
     * @param collectionName 集合名称
     * @param fieldName      查询字段
     * @return
     */
    @Override
    public List<String> distinct(String collectionName, String fieldName) {
        this.checkDistinctMongodbId(fieldName);
        return MongoExecutor.executeCollection(collectionName, (collection) -> {
            List<String> distinctList = new ArrayList<>();
            // 自动关闭游标
            try (MongoCursor<String> cursor = collection.distinct(fieldName, String.class).iterator()) {
                cursor.forEachRemaining(distinctList::add);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            return distinctList;
        });
    }

    /**
     * 校验是否对主键进行查重
     *
     * @param fieldName 字段名称
     */
    private void checkDistinctMongodbId(String fieldName) {
        if (MONGODB_ID.equals(fieldName)) {
            throw new RuntimeException("不支持对主键查重");
        }
    }

    /**
     * 获取指定字段的不重复值
     * <p>
     * 警告：不要对主键_id操作，返回实体类集合
     *
     * @param collectionName  集合名称
     * @param entityClazz     实体类，警告：不能是常规类型
     * @param fieldName       查重字段
     * @param filters         过滤条件
     * @param <MongodbEntity> mongodb实体类
     * @return
     */
    @Override
    public <MongodbEntity> List<MongodbEntity> distinctToEntityList(String collectionName, Class<MongodbEntity> entityClazz, String fieldName, Bson filters) {
        this.checkDistinctMongodbId(fieldName);
        return this.distinctToEntityList(collectionName, entityClazz, fieldName, filters, GSON_INCLUDE_NULL_TRANSFORM_ID, SETTINGS_TRANSFORM_ID);
    }

    /**
     * 聚合查询
     * <p>
     * 备注：若返回主键，则主键为字符串，例如："_id":"62a0a366510fd82f29e27c6f"
     *
     * @param collectionClazz 待查询的集合实体类，备注：必须包含实体类注解
     * @param pipelines       管道操作
     * @return
     */
    @Override
    public List<JsonObject> aggregate(Class<?> collectionClazz, List<Bson> pipelines) {
        return this.aggregate(collectionClazz, true, pipelines);
    }

    /**
     * 聚合查询
     * <p>
     * 备注：若返回主键，则主键为字符串，例如："_id":"62a0a366510fd82f29e27c6f"
     *
     * @param collectionName 集合名称
     * @param pipelines      管道操作
     * @return
     */
    @Override
    public List<JsonObject> aggregate(String collectionName, List<Bson> pipelines) {
        return this.aggregate(collectionName, true, pipelines);
    }

    /**
     * 聚合查询
     *
     * @param collectionClazz   待查询的集合实体类，备注：必须包含实体类注解
     * @param changeIdAttrToStr 是否改变主键属性
     *                          <p> 若changeIdName=true，主键值为字符串，例如："_id":"62a0a366510fd82f29e27c6f"。
     *                          <p> 若changeIdName=false，主键值为原始类型或为$oid，例如："_id":{"$oid":"62a0a366510fd82f29e27c6f"}
     * @param pipelines         管道操作
     * @return
     */
    @Override
    public List<JsonObject> aggregate(Class<?> collectionClazz, boolean changeIdAttrToStr, List<Bson> pipelines) {
        // 获取集合注解
        MongoCollectionName collectionName = collectionClazz.getAnnotation(MongoCollectionName.class);
        if (null == collectionName) {
            throw new RuntimeException("缺失mongodb集合实体类注解");
        }
        return this.aggregate(collectionName.value(), changeIdAttrToStr, pipelines);
    }

    /**
     * 聚合查询
     *
     * @param collectionName    集合名称
     * @param changeIdAttrToStr 是否改变主键属性
     *                          <p> 若changeIdName=true，主键值为字符串，例如："_id":"62a0a366510fd82f29e27c6f"。
     *                          <p> 若changeIdName=false，主键值为原始类型或为$oid，例如："_id":{"$oid":"62a0a366510fd82f29e27c6f"}
     * @param pipelines         管道操作
     * @return
     */
    @Override
    public List<JsonObject> aggregate(String collectionName, boolean changeIdAttrToStr, List<Bson> pipelines) {
        if (changeIdAttrToStr) {
            // 将mongodb主键_id的值，由对象转字符串
            return this.aggregateToEntityList(collectionName, JsonObject.class, pipelines, GSON_INCLUDE_NULL, SETTINGS_TRANSFORM_ID);
        }
        return this.aggregateToEntityList(collectionName, JsonObject.class, pipelines, GSON_INCLUDE_NULL, SETTINGS_DEFAULT);
    }

    /**
     * 聚合查询，返回mongodb实体类集合
     * <p>
     * 备注：若返回主键，则主键为字符串，例如："_id":"62a0a366510fd82f29e27c6f"
     *
     * @param collectionClazz 待查询的集合实体类
     *                        <p>备注：
     *                        <p>1.必须包含实体类注解@MongoCollectionName("集合名称")，
     *                        <p>2.主键必须包含@SerializedName("_id")或 @MongoId
     * @param pipelines       管道操作
     * @param <MongodbEntity> mongodb实体类
     * @return
     */
    @Override
    public <MongodbEntity> List<MongodbEntity> aggregateToEntityList(Class<MongodbEntity> collectionClazz, List<Bson> pipelines) {
        // 获取集合注解
        MongoCollectionName collectionName = collectionClazz.getAnnotation(MongoCollectionName.class);
        if (null == collectionName) {
            throw new RuntimeException("缺失mongodb集合实体类注解");
        }
        return this.aggregateToEntityList(collectionName.value(), collectionClazz, pipelines, GSON_INCLUDE_NULL_TRANSFORM_ID, SETTINGS_TRANSFORM_ID);
    }

    /**
     * 聚合查询，返回mongodb实体类集合
     *
     * @param collectionName 集合名称
     * @param entityClazz    实体类
     * @param pipelines      管道操作
     * @return
     */
    @Override
    public <MongodbEntity> List<MongodbEntity> aggregateToEntityList(String collectionName, Class<MongodbEntity> entityClazz, List<Bson> pipelines) {
        return this.aggregateToEntityList(collectionName, entityClazz, pipelines, GSON_INCLUDE_NULL_TRANSFORM_ID, SETTINGS_TRANSFORM_ID);
    }

    /**
     * 获取指定字段的不重复值
     *
     * @param collectionName  集合名称
     * @param entityClazz     实体类，警告：不能是常规类型
     * @param fieldName       查重字段
     * @param filter          过滤条件
     * @param gson            序列化方法
     * @param settings        配置
     * @param <MongodbEntity> 泛型
     * @return
     */
    private <MongodbEntity> List<MongodbEntity> distinctToEntityList(String collectionName, Class<MongodbEntity> entityClazz, String fieldName, Bson filter, Gson gson, JsonWriterSettings settings) {
        return MongoExecutor.executeCollection(collectionName, (collection) -> {
            List<MongodbEntity> distinctList = new ArrayList<>();
            MongoCursor<Document> cursor = null;
            try {
                DistinctIterable<Document> distinct = null;
                if (null != filter) {
                    distinct = collection.distinct(fieldName, filter, Document.class);
                } else {
                    distinct = collection.distinct(fieldName, Document.class);
                }
                cursor = distinct.cursor();
                while (cursor.hasNext()) {
                    distinctList.add(GSON_INCLUDE_NULL_TRANSFORM_ID.fromJson(cursor.next().toJson(SETTINGS_TRANSFORM_ID), entityClazz));
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            } finally {
                if (null != cursor) {
                    // 关闭游标
                    cursor.close();
                }
            }
            return distinctList;
        });
    }

    /**
     * 聚合查询
     *
     * @param collectionName  集合名称
     * @param collectionClazz 集合实体类
     * @param pipelines       管道操作
     * @param gson            序列化方法
     * @param settings        配置
     * @param <MongodbEntity> 泛型
     * @return
     */
    private <MongodbEntity> List<MongodbEntity> aggregateToEntityList(String collectionName, Class<MongodbEntity> collectionClazz, List<Bson> pipelines, Gson gson, JsonWriterSettings settings) {
        return MongoExecutor.executeCollection(collectionName, (collection) -> {
            List<MongodbEntity> dataList = new ArrayList<>();
            MongoCursor<Document> cursor = null;
            try {
                AggregateIterable<Document> aggregate = collection.aggregate(pipelines);
                // 使用系统缓存将数据写入临时文件
                aggregate.allowDiskUse(true);
                cursor = aggregate.iterator();
                while (cursor.hasNext()) {
                    dataList.add(gson.fromJson(cursor.next().toJson(settings), collectionClazz));
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            } finally {
                if (null != cursor) {
                    // 关闭游标
                    cursor.close();
                }
            }
            return dataList;
        });
    }

}
