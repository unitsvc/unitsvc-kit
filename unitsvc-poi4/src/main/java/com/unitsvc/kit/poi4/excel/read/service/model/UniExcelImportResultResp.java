package com.unitsvc.kit.poi4.excel.read.service.model;

import com.google.gson.JsonObject;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 功能描述：
 *
 * @author : jun
 * @version : v1.0.0
 * @date : 2022/12/7 20:31
 **/
@Data
public class UniExcelImportResultResp implements Serializable {

    private static final long serialVersionUID = 2004828248036167239L;

    /**
     * 已匹配的可编辑导入列
     */
    private List<String> readEditNameCnList;

    /**
     * 读取的导入数据列表，必填
     */
    private List<JsonObject> dataList;

    // --------------------------------------------------- 2022-12-09 16:12 额外业务字段信息 ---------------------------------
    /**
     * 工作表序号，必填
     */
    private Integer sheetIndex;

    /**
     * 表头元数据，必填
     */
    private TaskSheetMeta taskSheetMeta;

    /**
     * 定义额外的属性，必填
     * <p>
     * 说明：额外的字段属性列表，主要用于查询表格数据时排除该额外字段，示例：__sheetIndex，_id，__checkId，__checkDate，__month，__sheetIndex
     */
    private List<String> extraDataAttrs;

}
