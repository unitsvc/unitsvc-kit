package com.unitsvc.kit.poi4.excel.write.style;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;

/**
 * 功能描述：自定义单元格样式接口，实现该接口可自定义表格样式
 *
 * @author : jun
 * @version : v1.0.0
 * @date : 2022/10/14 11:37
 */
public interface IUniExcelCustomStyle {

    // ************************************************************* 标题样式控制 *************************************************************

    /**
     * 设置标题标题样式，示例：方案汇总
     *
     * @param font  字体
     * @param style 样式
     */
    void setTitleHeaderStyle(Font font, CellStyle style);

    // ************************************************************* 分组表头样式控制 ***************************************************
    /**
     * 设置分组列表头样式，示例：过渡期方案（旧）、过渡期方案（新）
     *
     * @param font  字体
     * @param style 样式
     */
    void setGroupHeaderStyle(Font font, CellStyle style);

    // ****************************************************** 数据表头样式控制 *************************************************************

    /**
     * 设置数据列可编辑表头样式，，示例：工号
     *
     * @param font  字体
     * @param style 样式
     */
    void setDataHeaderEditStyle(Font font, CellStyle style);

    /**
     * 设置数据列只读表头样式，示例：姓名
     *
     * @param font  字体
     * @param style 样式
     */
    void setDataHeaderReadonlyStyle(Font font, CellStyle style);

    // ***************************************************** 表格数据单元格样式控制 *****************************************************

    /**
     * 设置可编辑数据单元格样式，示例：张三、20
     *
     * @param font  字体
     * @param style 样式
     */
    void setDataCellEditStyle(Font font, CellStyle style);

    /**
     * 设置只读数据单元格样式，示例：折算系数
     *
     * @param font  字体
     * @param style 样式
     */
    void setDateCellReadonlyStyle(Font font, CellStyle style);

    /**
     * 设置可编辑数据单元格样式（汇总行），示例：总计，小计
     *
     * @param font  字体
     * @param style 样式
     */
    void setDataCellEditSummaryMarkStyle(Font font, CellStyle style);

    /**
     * 设置只读数据单元格样式（汇总行），示例：总计，小计
     *
     * @param font  字体
     * @param style 样式
     */
    void setDateCellReadonlySummaryMarkStyle(Font font, CellStyle style);

}
