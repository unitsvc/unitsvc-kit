package com.unitsvc.kit.facade.generate.dtos;

import com.unitsvc.kit.facade.generate.schema.common.BaseInfo;
import com.unitsvc.kit.facade.generate.schema.field.FieldConfig;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * 功能描述：数据表信息类
 * <p>
 * 说明：自动化工具帮助类
 *
 * @author : coder
 * @version : v1.0.0
 * @since : 2023/11/7 15:39
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class TableInfo extends BaseInfo implements Serializable {

    private static final long serialVersionUID = 9070446987811554593L;

    /**
     * 最后更新时间
     */
    private long lastRefreshTime;

    /**
     * 数据列定义
     */
    private List<FieldConfig> columns;

    /**
     * 可查看的数据列编号列表
     */
    private List<String> showColumns;

    /**
     * 是否可以新增
     */
    private boolean canAdd;

    /**
     * 是否可以编辑
     */
    private boolean canEdit;

    /**
     * 是否可以删除
     */
    private boolean canDelete;

    /**
     * 可以编辑的字段
     */
    private List<String> editFields;

    /**
     * 是否是owner
     */
    private Boolean isOwner;

}

