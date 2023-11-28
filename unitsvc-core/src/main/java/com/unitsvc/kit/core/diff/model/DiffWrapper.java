package com.unitsvc.kit.core.diff.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 功能描述：复杂对象比对
 *
 * @author : jun
 * @version : v1.0.0
 * @since : 2023/6/27 18:21
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DiffWrapper implements Serializable {

    private static final long serialVersionUID = 2301281216920076401L;

    /**
     * 字段路径
     */
    private String path;

    /**
     * 字段名称
     */
    private String name;

    /**
     * 字段类型
     */
    private String type;

    /**
     * 操作方法
     * <p>
     * 说明：新增，修改，删除
     */
    private String operate = "";

    /**
     * 数据变更
     */
    private Difference diffValue;

}