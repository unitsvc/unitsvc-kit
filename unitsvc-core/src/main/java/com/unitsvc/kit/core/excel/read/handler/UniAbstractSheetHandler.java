package com.unitsvc.kit.core.excel.read.handler;

import com.unitsvc.kit.core.excel.enums.UniExcelCellTypeEnum;

/**
 * 功能描述：表格导入抽象类
 *
 * @author : jun
 * @version : v1.0.0
 * @date : 2023/2/23 17:30
 **/
public abstract class UniAbstractSheetHandler {

    /**
     * 开始读取行数据
     *
     * @param rowNum     行号，序号从0开始
     * @param sheetIndex 工作表序号，序号从0开始
     * @param sheetName  工作表名称
     */
    public abstract void startRowRead(int rowNum, int sheetIndex, String sheetName);

    /**
     * 结束行数据读取
     *
     * @param rowNum     行号，序号从0开始
     * @param sheetIndex 工作表序号，序号从0开始
     * @param sheetName  工作表名称
     */
    public abstract void endRowRead(int rowNum, int sheetIndex, String sheetName);

    /**
     * 单元格数据读取
     *
     * @param cellRef    单元格指向，示例：A1
     * @param rowNum     行号，序号从0开始
     * @param sheetIndex 工作表序号，序号从0开始
     * @param sheetName  工作表名称
     * @param colNum     列号，序号从0开始
     * @param cellType   单元格类型
     * @param cellValue  单元格数值，说明：若为null，则不存在
     * @param fmtValue   格式化展示值
     * @param formula    公式，说明：若为null，则不存在
     */
    public abstract void cellRead(String cellRef, int rowNum, Integer sheetIndex, String sheetName, int colNum, UniExcelCellTypeEnum cellType, String cellValue, String fmtValue, String formula);

}
