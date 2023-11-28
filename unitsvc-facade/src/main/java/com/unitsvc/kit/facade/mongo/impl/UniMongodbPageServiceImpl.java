package com.unitsvc.kit.facade.mongo.impl;

import com.jpardus.db.jmongo.MongoQuerier;
import com.jpardus.db.jmongo.QueryCondition;
import com.jpardus.spider.facade.bean.PagedList;
import com.unitsvc.kit.facade.mongo.IUniMongodbPageService;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * 功能描述：
 *
 * @author : jun
 * @version : v1.0.0
 * @date : 2022/11/30 9:36
 **/
@Service
public class UniMongodbPageServiceImpl implements IUniMongodbPageService {

    /**
     * mongodb分页操作
     * <p>
     * 示例：
     * QueryCondition queryCondition =new QueryCondition();
     * queryCondition.setFilter();
     * queryCondition.setSort();
     * queryCondition.setLimitAndPage(pageNum,pageSize);
     * queryCondition.setProjection();
     *
     * @param modelClazz    模型对象类
     * @param pageCondition 查询条件
     * @return
     */
    @Override
    public <MongodbModel> PagedList<MongodbModel> mongodbPage(Class<MongodbModel> modelClazz, QueryCondition pageCondition) {
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

}
