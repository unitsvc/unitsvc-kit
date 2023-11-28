package com.unitsvc.kit.facade.generate.schema.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 功能描述：快照内容，用于分环境处理
 *
 * @author : coder
 * @version : v1.0.0
 * @since : 2023/11/7 23:09
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class SnapshotContainer<T> implements Serializable {
    private static final long serialVersionUID = 3407562489223047129L;
    /**
     * 开发环境快照
     */
    private T dev;

    /**
     * 开发环境版本号
     */
    private String devVersion;

    /**
     * 开发环境配置，成员即可修改。
     */
    private String devConfig;

    /**
     * 测试环境快照
     */
    private T test;

    /**
     * 测试环境版本号
     */
    private String testVersion;

    /**
     * 测试环境配置，应用负责人及测试负责人可修改。
     */
    private String testConfig;

    /**
     * 集成环境快照
     */
    private T stg;

    /**
     * 集成环境版本号
     */
    private String stgVersion;

    /**
     * stg环境配置，只有应用负责人可以编辑
     */
    private String stgConfig;

    /**
     * 生产环境版本号
     */
    private String prodVersion;

    /**
     * 生产配置，只有应用负责人可以编辑
     */
    private String prodConfig;

}
