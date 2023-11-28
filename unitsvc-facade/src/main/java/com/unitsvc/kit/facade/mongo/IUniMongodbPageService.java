package com.unitsvc.kit.facade.mongo;

import com.jpardus.db.jmongo.QueryCondition;
import com.jpardus.spider.facade.bean.PagedList;

/**
 * 功能描述：
 *
 * @author : jun
 * @version : v1.0.0
 * @date : 2022/11/30 9:35
 **/
public interface IUniMongodbPageService {

    /**
     * mongodb分页操作
     * <p>
     * 示例：
     * QueryCondition queryCondition =new QueryCondition();
     * queryCondition.setFilter();
     * queryCondition.setSort();
     * queryCondition.setLimitAndPage(pageSize,pageNum);
     * queryCondition.setProjection();
     *
     * @param modelClazz    模型对象类
     * @param pageCondition 查询条件
     * @return
     */
    <MongodbModel> PagedList<MongodbModel> mongodbPage(Class<MongodbModel> modelClazz, QueryCondition pageCondition);

}
