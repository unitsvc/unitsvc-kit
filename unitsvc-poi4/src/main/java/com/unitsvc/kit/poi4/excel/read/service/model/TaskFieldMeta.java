package com.unitsvc.kit.poi4.excel.read.service.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 功能描述：定义任务字段基础元数据
 *
 * @author : jun
 * @version : v1.0.0
 * @date : 2022/12/7 19:21
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskFieldMeta implements Serializable {

    private static final long serialVersionUID = 3408011058717448078L;

    /**
     * 字段ID
     */
    private String fieldId;

    /**
     * 字段中文名称
     */
    private String nameCn;

    /**
     * 字段英文名称
     */
    private String nameEn;

}
