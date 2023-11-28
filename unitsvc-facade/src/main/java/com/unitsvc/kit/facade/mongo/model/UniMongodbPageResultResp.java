package com.unitsvc.kit.facade.mongo.model;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 功能描述：
 *
 * @author : jun
 * @version : v1.0.0
 * @date : 2022/11/30 10:26
 **/
@Data
public class UniMongodbPageResultResp<Model> implements Serializable {

    private static final long serialVersionUID = 4757484093686885121L;

    /**
     * 第几页
     */
    private Integer pageNum;

    /**
     * 每页记录数
     */
    private Integer pageSize;

    /**
     * 总页数
     */
    private Integer page;

    /**
     * 总条数
     */
    private Integer total;

    /**
     * 分页数据
     */
    private List<Model> records;

    /**
     * @param pageNum    第几页
     * @param pageSize   每页记录数
     * @param totalCount 总条数
     * @param records    分页数据
     */
    public UniMongodbPageResultResp(Integer pageNum, Integer pageSize, Integer totalCount, List<Model> records) {
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.total = totalCount;
        this.records = records;
        // 总页数
        this.page = totalCount % pageSize == 0 ? totalCount / pageSize : totalCount / pageSize + 1;
    }

}
