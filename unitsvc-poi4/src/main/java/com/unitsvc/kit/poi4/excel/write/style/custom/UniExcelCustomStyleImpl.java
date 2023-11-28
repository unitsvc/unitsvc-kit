package com.unitsvc.kit.poi4.excel.write.style.custom;

import com.unitsvc.kit.poi4.excel.write.style.IUniExcelCustomStyle;
import com.unitsvc.kit.poi4.excel.write.utils.UniExcelCellStyleUtil;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.VerticalAlignment;

/**
 * 功能描述：自定义导出样式，自动换行
 *
 * @author : jun
 * @version : v1.0.0
 * @date : 2022/10/14 15:05
 */
public class UniExcelCustomStyleImpl implements IUniExcelCustomStyle {

    /**
     * 设置标题标题样式，示例：方案汇总
     *
     * @param font  字体
     * @param style 样式
     */
    @Override
    public void setTitleHeaderStyle(Font font, CellStyle style) {
        // 居中
        UniExcelCellStyleUtil.setCellCenterStyle(style);
        // 细边框
        UniExcelCellStyleUtil.setCellBorderThinStyle(style);
        // 宋体、22、加粗
        UniExcelCellStyleUtil.setCellTitleHeaderDefaultFont(font, style);
        // 关闭标题单元格保护
        style.setLocked(false);
        // 开启自动换行
        style.setWrapText(true);
    }

    /**
     * 设置分组列表头样式，示例：过渡期方案（旧）、过渡期方案（新）
     *
     * @param font  字体
     * @param style 样式
     */
    @Override
    public void setGroupHeaderStyle(Font font, CellStyle style) {
        // 居中
        UniExcelCellStyleUtil.setCellCenterStyle(style);
        // 灰色
        UniExcelCellStyleUtil.setCellGreyStyle(style);
        // 细边框
        UniExcelCellStyleUtil.setCellBorderThinStyle(style);
        // 字体样式
        UniExcelCellStyleUtil.setCellFont(font, style, "宋体", 10, true);
        // 关闭分组表头单元格保护
        style.setLocked(false);
        // 开启自动换行
        style.setWrapText(true);
    }

    /**
     * 设置数据列可编辑表头样式，，示例：工号
     *
     * @param font  字体
     * @param style 样式
     */
    @Override
    public void setDataHeaderEditStyle(Font font, CellStyle style) {
        // 居中
        UniExcelCellStyleUtil.setCellCenterStyle(style);
        // 细边框
        UniExcelCellStyleUtil.setCellBorderThinStyle(style);
        // 设置字体样式
        UniExcelCellStyleUtil.setCellFont(font, style, "宋体", 10, true);
        // 关闭自动换行
        style.setWrapText(false);
    }

    /**
     * 设置数据列只读表头样式，示例：姓名
     *
     * @param font  字体
     * @param style 样式
     */
    @Override
    public void setDataHeaderReadonlyStyle(Font font, CellStyle style) {
        // 居中
        UniExcelCellStyleUtil.setCellCenterStyle(style);
        // 灰色
        UniExcelCellStyleUtil.setCellGreyStyle(style);
        // 细边框
        UniExcelCellStyleUtil.setCellBorderThinStyle(style);
        // 设置字体样式
        UniExcelCellStyleUtil.setCellFont(font, style, "宋体", 10, true);
        // 关闭自动换行
        style.setWrapText(false);
    }

    /**
     * 设置可编辑数据单元格样式，示例：张三、20
     *
     * @param font  字体
     * @param style 样式
     */
    @Override
    public void setDataCellEditStyle(Font font, CellStyle style) {
        // 细边框
        UniExcelCellStyleUtil.setCellBorderThinStyle(style);
        // 允许编辑
        style.setLocked(false);
        // 关闭自动换行
        style.setWrapText(false);
    }

    /**
     * 设置只读数据单元格样式，示例：折算系数
     *
     * @param font  字体
     * @param style 样式
     */
    @Override
    public void setDateCellReadonlyStyle(Font font, CellStyle style) {
        // 灰色
        UniExcelCellStyleUtil.setCellGreyStyle(style);
        // 细边框
        UniExcelCellStyleUtil.setCellBorderThinStyle(style);
        // 不可编辑
        style.setLocked(true);
        // 关闭自动换行
        style.setWrapText(false);
    }

    /**
     * 设置可编辑数据单元格样式（汇总行），示例：总计，小计
     *
     * @param font  字体
     * @param style 样式
     */
    @Override
    public void setDataCellEditSummaryMarkStyle(Font font, CellStyle style) {
        // 细边框
        UniExcelCellStyleUtil.setCellBorderThinStyle(style);
        // 允许编辑
        style.setLocked(false);
        // 关闭自动换行
        style.setWrapText(false);

        // 默认字体
        font.setFontName("等线");
        // 字体加粗
        font.setBold(true);
        // 设置字体
        style.setFont(font);

        // 垂直-居中
        style.setVerticalAlignment(VerticalAlignment.CENTER);
    }

    /**
     * 设置只读数据单元格样式（汇总行），示例：总计，小计
     *
     * @param font  字体
     * @param style 样式
     */
    @Override
    public void setDateCellReadonlySummaryMarkStyle(Font font, CellStyle style) {
        // 灰色
        UniExcelCellStyleUtil.setCellGreyStyle(style);
        // 细边框
        UniExcelCellStyleUtil.setCellBorderThinStyle(style);
        // 不可编辑
        style.setLocked(true);
        // 关闭自动换行
        style.setWrapText(false);

        // 默认字体
        font.setFontName("等线");
        // 字体加粗
        font.setBold(true);
        // 设置字体
        style.setFont(font);

        // 垂直-居中
        style.setVerticalAlignment(VerticalAlignment.CENTER);
    }
}
