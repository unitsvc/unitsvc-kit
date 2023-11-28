package com.unitsvc.kit.facade.generate.schema.label;

import com.unitsvc.kit.facade.generate.schema.common.BaseInfoExtend;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * 功能描述：标签组
 *
 * @author : coder
 * @version : v1.0.0
 * @since : 2023/11/7 23:29
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class LabelGroup extends BaseInfoExtend implements Serializable {
    private static final long serialVersionUID = 4464692086423618648L;
    /**
     * 标记或用途，通常用来区分使用场景
     */
    private List<String> marks;

    /**
     * 授权应用编号，授权应用可以在此组下对标签做crud操作
     */
    private List<String> apps;
}
