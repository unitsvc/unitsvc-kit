package com.unitsvc.kit.facade.generate.schema.label;

import com.google.gson.JsonObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 功能描述：标签数据对象
 *
 * @author : coder
 * @version : v1.0.0
 * @since : 2023/11/7 23:29
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class LabelData implements Serializable {
    private static final long serialVersionUID = 4572813100048224298L;
    /**
     * 编号
     */
    private String id;

    /**
     * 标签编号
     */
    private String labelId;

    /**
     * 标签对象编号
     */
    private String targetId;

    /**
     * 关联属性，比如部门编号、职位编号等等，表示特定条件下的标签，如某个部门的BP。
     */
    private JsonObject relations;
}

