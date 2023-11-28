package com.unitsvc.kit.facade.datasource.table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 功能描述：H2数据表信息
 *
 * @author : jun
 * @version : v1.0.0
 * @since : 2023/6/20 15:00
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TableInfo implements Serializable {

    private static final long serialVersionUID = 8435351126925139830L;

    /**
     * 必填，数据表名称
     */
    private String tableName;

    /**
     * 必填，主键名称
     * <pre>
     * 说明：该主键标识元数据真实主键。
     * 示例：_id
     * </pre>
     */
    private String primaryId = "";

    /**
     * 必填，主键别名
     * <pre>
     * 说明：该主键别名元数据真实主键。
     * 示例：id
     * </pre>
     */
    private String primaryAlias = "";

    /**
     * 可选，是否启用临时会话
     * <p>
     * 说明：若enableSession=true，启用临时会话，则额外新增会话字段，会话主键。
     */
    private Boolean enableSession = false;

    /**
     * 可选，会话主键ID
     * <pre>
     * 说明：
     * 1.若enableSession=true，启用临时会话，则创建会话主键字段，可修改字段名称。
     * 2.虚拟主键，在临时会话中，将会使真实主键失效。
     * </pre>
     */
    private String sessionKey = "__session_key__";

    /**
     * 可选，临时会话ID
     * <p>
     * 说明：若enableSession=true，启用临时会话，则创建会话ID，可修改字段名称。
     */
    private String sessionId = "__session_id__";

    /**
     * 必填，数据列信息
     */
    private List<ColumnInfo> columns;

}
