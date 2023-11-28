package com.unitsvc.kit.poi4.excel.read.handler;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFComment;

import java.util.Date;

/**
 * 功能描述：自定义处理器
 *
 * @author : jun
 * @version : v1.0.0
 * @date : 2022/12/16 18:08
 **/
@SuppressWarnings("all")
public class UniCustomSheetHandler implements UniV4XSSFSheetXMLHandler.SheetContentsHandler {

    /**
     * 定义行数据模型
     */
    private JsonObject rowModel;

    @Override
    public void startRow(int rowNum, Integer customExtSheetIndex, String customExtSheetName) {
        // pass
        if (rowNum >= 0) {
            // 开始读取数据时，初始化行数据
            rowModel = new JsonObject();
        }
    }

    @Override
    public void endRow(int rowNum, Integer customExtSheetIndex, String customExtSheetName) {
        // pass
        if (rowNum >= 0) {
            // 记录单元格行序号
            rowModel.addProperty("rowNum", rowNum + 1);
            // 记录工作表序号
            rowModel.addProperty("sheetIndex", customExtSheetIndex);
            // 记录工作表名称
            rowModel.addProperty("sheetName", customExtSheetName);
        }
        if (null != rowModel) {
            System.out.printf("第%s行数据：\n%s%n", rowNum + 1, new GsonBuilder().serializeNulls().setPrettyPrinting().create().toJson(rowModel));
        }
    }

    @Override
    public void cell(String cellReference, String formattedValue, XSSFComment comment, Integer customExtRowIndex, Integer customExtColIndex, String customExtColTitle, UniV4XSSFSheetXMLHandler.xssfDataType customExtXssfDataType, XSSFCellStyle customExtStyle, String customExtOriginValue, String customExtFormula, Integer customExtSheetIndex, String customExtSheetName) {
        if (null != comment) {
            // 去除批注逻辑
            return;
        }
        switch (customExtXssfDataType) {
            case BOOLEAN:
                // 布尔
                break;
            case NUMBER:
                // 数字
                // 校验是否时间格式化
                boolean checkIsDataFormat = DateUtil.isADateFormat(customExtStyle.getIndex(), customExtStyle.getDataFormatString());
                if (checkIsDataFormat) {
                    // 单元格实际为时间类型
                    // 转换为java时间
                    Date date = DateUtil.getJavaDate(Double.parseDouble(customExtOriginValue), false);
                    System.out.println("date = " + date);
                } else {
                    // 单元格实际为数字类型
                    // TODO
                }
                break;
            case SST_STRING:
                // 字符串
                break;
            default:
        }

        if (null != comment && null != rowModel) {
            // 源数据、格式化值、格式化信息、公式值、批注
            rowModel.addProperty(customExtColTitle + ":" + customExtColIndex + ":COMMENT", String.format("【%s】【%s】【%s】【%s】【%s】【%s】", customExtXssfDataType, customExtOriginValue, formattedValue, (null == customExtStyle ? "" : customExtStyle.getDataFormatString()), customExtFormula, comment.getString().toString()));
        }
        // pass
        if (rowModel != null) {
            // 源数据、格式化值、格式化信息、公式值
            rowModel.addProperty(customExtColTitle + ":" + customExtColIndex, String.format("【%s】【%s】【%s】【%s】【%s】", customExtXssfDataType, customExtOriginValue, formattedValue, (null == customExtStyle ? "" : customExtStyle.getDataFormatString()), customExtFormula));
        }
    }


}
