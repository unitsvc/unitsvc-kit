package com.unitsvc.kit.facade.generate.schema.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 功能描述：基础信息类
 * <p>
 * 说明：自动化工具帮助类
 *
 * @author : coder
 * @version : v1.0.0
 * @since : 2023/11/7 15:36
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public abstract class BaseInfo {

    /**
     * 编号
     */
    private String id;

    /**
     * 名称
     */
    private String name;

    /**
     * 详细说明
     */
    private String description;

    /**
     * 创建人标识，域帐号
     */
    private String creatorId;

    /**
     * 创建时间
     */
    private Long createTime;

    /**
     * 更新时间
     */
    private Long updateTime;

}
