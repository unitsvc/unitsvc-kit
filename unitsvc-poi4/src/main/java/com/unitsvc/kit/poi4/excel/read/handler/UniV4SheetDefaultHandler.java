package com.unitsvc.kit.poi4.excel.read.handler;

import cn.hutool.core.util.StrUtil;
import com.unitsvc.kit.core.excel.enums.UniExcelCellTypeEnum;
import com.unitsvc.kit.core.excel.read.handler.UniAbstractSheetHandler;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.BuiltinFormats;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFComment;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 功能描述：默认表格处理器
 *
 * @author : jun
 * @version : v1.0.0
 * @date : 2022/12/16 18:08
 **/
@SuppressWarnings("all")
public abstract class UniV4SheetDefaultHandler extends UniAbstractSheetHandler implements UniV4XSSFSheetXMLHandler.SheetContentsHandler {


    @Override
    public void startRow(int rowNum, Integer customExtSheetIndex, String customExtSheetName) {
        // 回调方法
        this.startRowRead(rowNum, customExtSheetIndex, customExtSheetName);
    }

    @Override
    public void endRow(int rowNum, Integer customExtSheetIndex, String customExtSheetName) {
        // 回调方法
        this.endRowRead(rowNum, customExtSheetIndex, customExtSheetName);
    }

    @Override
    public void cell(String cellReference, String formattedValue, XSSFComment comment, Integer customExtRowIndex, Integer customExtColIndex, String customExtColTitle, UniV4XSSFSheetXMLHandler.xssfDataType customExtXssfDataType, XSSFCellStyle customExtStyle, String customExtOriginValue, String customExtFormula, Integer customExtSheetIndex, String customExtSheetName) {
        // 不读取批注内容
        if (null != comment) {
            // 去除批注逻辑
            return;
        }
        // 默认文本类型
        UniExcelCellTypeEnum cellTypeEnum = UniExcelCellTypeEnum.STRING;
        String cellValue = customExtOriginValue;
        switch (customExtXssfDataType) {
            case BOOLEAN:
                cellTypeEnum = UniExcelCellTypeEnum.BOOLEAN;
                if (StrUtil.equals(customExtOriginValue, "0")) {
                    cellValue = "false";
                } else if (StrUtil.equals(customExtOriginValue, "1")) {
                    cellValue = "true";
                } else {
                    cellValue = null;
                }
                // 布尔
                break;
            case NUMBER:
                // 数字
                // -----------------------------------------------------------------------------------------------------
                short formatIndex = customExtStyle.getDataFormat();
                String formatString = customExtStyle.getDataFormatString();
                if (formatString == null) {
                    formatString = BuiltinFormats.getBuiltinFormat(formatIndex);
                }
                // -----------------------------------------------------------------------------------------------------
                // 校验是否时间格式化
                boolean checkIsData = DateUtil.isADateFormat(formatIndex, formatString);
                if (checkIsData) {
                    cellTypeEnum = UniExcelCellTypeEnum.DATE;
                    // 单元格实际为时间类型
                    // 转换为java时间
                    if (StringUtils.isNotEmpty(customExtOriginValue)) {
                        Date date = DateUtil.getJavaDate(Double.parseDouble(customExtOriginValue), false);
                        cellValue = String.valueOf(date.getTime());
                    } else {
                        cellValue = null;
                    }
                } else {
                    // 备注：货币类型也是数值类型
                    cellTypeEnum = UniExcelCellTypeEnum.NUMBER;
                    // 单元格实际为数字类型
                    if (StringUtils.isNotEmpty(customExtOriginValue)) {
                        cellValue = new BigDecimal(customExtOriginValue).toPlainString();
                    } else {
                        cellValue = null;
                    }
                }
                break;
            case INLINE_STRING:
                // 富文本
            case SST_STRING:
                // 标准文本
                cellTypeEnum = UniExcelCellTypeEnum.STRING;
                cellValue = customExtOriginValue;
                break;
            case FORMULA:
                // 公式
                cellTypeEnum = UniExcelCellTypeEnum.FORMULA;
                cellValue = customExtOriginValue;
                break;
            case ERROR:
                // 错误
                cellTypeEnum = UniExcelCellTypeEnum.ERROR;
                cellValue = customExtOriginValue;
            default:
        }
        // 回调方法
        this.cellRead(cellReference, customExtRowIndex, customExtSheetIndex, customExtSheetName, customExtColIndex, cellTypeEnum, cellValue, formattedValue, customExtFormula);
    }

}
