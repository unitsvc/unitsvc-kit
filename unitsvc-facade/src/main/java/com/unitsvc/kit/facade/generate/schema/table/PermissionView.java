package com.unitsvc.kit.facade.generate.schema.table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * 功能描述：授权视图
 *
 * @author : coder
 * @version : v1.0.0
 * @since : 2023/11/7 22:43
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class PermissionView implements Serializable {

    private static final long serialVersionUID = 7415438518301852994L;

    /**
     * 视图编号
     */
    private String id;

    /**
     * 视图名称
     */
    private String name;

    /**
     * 视图描述
     */
    private String description;

    /**
     * 是否完全开放，如果否，则根据授权应用开放
     */
    private boolean open;

    /**
     * 授权的应用列表，数据相关的接口方法只允许授权应用调用
     */
    private List<String> apps;

    /**
     * 表格授权集合，以标签（用户）为载体的授权模式
     */
    private List<TablePermission> permissions;

}
