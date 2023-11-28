package com.unitsvc.kit.facade.generate.schema.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 功能描述：扩展的基础信息类
 *
 * @author : coder
 * @version : v1.0.0
 * @since : 2023/11/7 22:53
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public abstract class BaseInfoExtend extends BaseInfo {
    /**
     * 归属应用
     */
    private String app;
}
