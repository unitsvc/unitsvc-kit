package com.unitsvc.kit.poi4.excel.read.service.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 功能描述：定义单元格工作表元信息
 *
 * @author : jun
 * @version : v1.0.0
 * @date : 2022/12/9 13:28
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskSheetMeta implements Serializable {

    private static final long serialVersionUID = -6891275637158220580L;

    /**
     * 工作表序号，必填
     */
    private int sheetIndex;

    /**
     * 工作表名称，可选
     */
    private String sheetName;

    /**
     * 表头元信息，必填
     */
    private List<TaskHeaderMeta> headerMetas;

}
