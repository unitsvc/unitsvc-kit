package com.unitsvc.kit.facade.generate.schema.label;

import com.unitsvc.kit.facade.generate.schema.common.BaseInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * 功能描述：标签类型
 *
 * @author : coder
 * @version : v1.0.0
 * @since : 2023/11/7 23:30
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class LabelType extends BaseInfo implements Serializable {
    private static final long serialVersionUID = 3186961425917225692L;
    /**
     * 标签对象表
     */
    private String foreign;

    /**
     * 标签对象表展示字段
     */
    private List<String> foreignShowFields;

    /**
     * 标签对象表查询字段
     */
    private List<String> foreignSearchFields;
}
