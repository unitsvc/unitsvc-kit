package com.unitsvc.kit.facade.mongo;

import com.google.gson.JsonObject;
import org.bson.conversions.Bson;

import java.util.List;

/**
 * 功能描述：mongodb通用服务
 *
 * @author : jun
 * @version : v1.0.0
 * @date : 2022/10/27 16:18
 **/
public interface IUniMongodbOpService {

    /**
     * 获取指定字段的不重复值，
     * <p>
     * 警告：不要对主键_id操作
     *
     * @param collectionClazz 待查询的集合实体类，备注：必须包含实体类注解
     * @param fieldName       查重字段
     * @param filter          过滤条件
     * @return
     */
    List<String> distinct(Class<?> collectionClazz, String fieldName, Bson filter);

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
    List<String> distinct(String collectionName, String fieldName, Bson filter);

    /**
     * 获取指定字段的不重复值
     * <p>
     * 警告：不要对主键_id操作
     *
     * @param collectionClazz 待查询的集合实体类，备注：必须包含实体类注解
     * @param fieldName       查重字段
     * @return
     */
    List<String> distinct(Class<?> collectionClazz, String fieldName);

    /**
     * 获取指定字段的不重复值
     * <p>
     * 警告：不要对主键_id操作
     *
     * @param collectionName 集合名称
     * @param fieldName      查询字段
     * @return
     */
    List<String> distinct(String collectionName, String fieldName);

    /**
     * 获取指定字段的不重复值，返回实体类集合
     * <p>
     * 警告：不要对主键_id操作
     *
     * @param collectionName  集合名称
     * @param entityClazz     实体类，警告：不能是常规类型
     * @param fieldName       查重字段
     * @param filters         过滤条件
     * @param <MongodbEntity> mongodb实体类
     * @return
     */
    <MongodbEntity> List<MongodbEntity> distinctToEntityList(String collectionName, Class<MongodbEntity> entityClazz, String fieldName, Bson filters);

    /**
     * 聚合查询
     * <p>
     * 备注：若返回主键，则主键为字符串，例如："_id":"62a0a366510fd82f29e27c6f"
     *
     * @param collectionClazz 待查询的集合实体类，备注：必须包含实体类注解
     * @param pipelines       管道操作
     * @return
     */
    List<JsonObject> aggregate(Class<?> collectionClazz, List<Bson> pipelines);

    /**
     * 聚合查询
     * <p>
     * 备注：若返回主键，则主键为字符串，例如："_id":"62a0a366510fd82f29e27c6f"
     *
     * @param collectionName 集合名称
     * @param pipelines      管道操作
     * @return
     */
    List<JsonObject> aggregate(String collectionName, List<Bson> pipelines);

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
    List<JsonObject> aggregate(Class<?> collectionClazz, boolean changeIdAttrToStr, List<Bson> pipelines);

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
    List<JsonObject> aggregate(String collectionName, boolean changeIdAttrToStr, List<Bson> pipelines);

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
    <MongodbEntity> List<MongodbEntity> aggregateToEntityList(Class<MongodbEntity> collectionClazz, List<Bson> pipelines);

    /**
     * 聚合查询，返回mongodb实体类集合
     *
     * @param collectionName  集合名称
     * @param entityClazz     实体类
     * @param pipelines       管道操作
     * @param <MongodbEntity> mongodb实体类
     * @return
     */
    <MongodbEntity> List<MongodbEntity> aggregateToEntityList(String collectionName, Class<MongodbEntity> entityClazz, List<Bson> pipelines);

}
