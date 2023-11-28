package com.unitsvc.kit.facade.generate.schema.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 功能描述：审核容器
 *
 * @author : coder
 * @version : v1.0.0
 * @since : 2023/11/7 23:05
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class ApproveContainer<T> implements Serializable {
    private static final long serialVersionUID = -335089333854552325L;
    /**
     * 审核内容
     */
    private T inst;

    /**
     * 审核状态
     * 0：草稿，当内容发生变化时，状态置为草稿
     * 1：待审核，应用管理员或超管进行审核，审核过程中内容不可更改
     * 9：审核通过，当内容审核通过时，状态置为审核通过
     */
    private int status;
}
