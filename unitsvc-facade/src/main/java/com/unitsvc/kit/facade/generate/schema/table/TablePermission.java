package com.unitsvc.kit.facade.generate.schema.table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * 功能描述：数据授权
 *
 * @author : coder
 * @version : v1.0.0
 * @since : 2023/11/7 22:44
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class TablePermission implements Serializable {

    private static final long serialVersionUID = -1175260362100280366L;

    /**
     * 授权的标签编号，数据授权的标签必须是用户对象标签，同一个表下标签不可重复
     */
    private String label;

    /**
     * 比较运算字符串，用来限制数据查看及修改的范围
     * 如"$a<$$b","$a<$$b&&$a>$$b"，$a表示数据字段a，$$b表示标签扩展字段b
     * =：等于，<：小于，>：大于，!=：不等于，<=：小于或等于，>=：大于或等于，@：属于字符串子串或数组项
     * &&：与，||：或，!：非
     */
    private String comparison;

    /**
     * 是否所有字段可见
     */
    private boolean allFields;

    /**
     * 可查看的数据字段集，TODO:后续替换为逻辑字段权限
     */
    private List<String> fields;

    /**
     * 是否可增加
     */
    private boolean canAdd;

    /**
     * 是否可编辑
     */
    private boolean canEdit;

    /**
     * 是否可删除
     */
    private boolean canDelete;

    /**
     * 是否所有字段可编辑
     */
    private boolean editAllFields;

    /**
     * 可编辑的数据字段集
     */
    private List<String> editFields;

}
