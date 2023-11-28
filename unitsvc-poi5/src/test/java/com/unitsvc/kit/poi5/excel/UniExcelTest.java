package com.unitsvc.kit.poi5.excel;

import com.unitsvc.kit.core.excel.enums.UniExcelCellTypeEnum;
import com.unitsvc.kit.poi5.excel.read.UniV5Excel2007ReadService;
import com.unitsvc.kit.poi5.excel.read.handler.UniV5SheetDefaultHandler;
import org.junit.Test;

/**
 * 功能描述：表格导入导出测试
 *
 * @author : jun
 * @version : v1.0.0
 * @date : 2023/2/23 11:03
 **/
public class UniExcelTest {

    /**
     * 表格读取测试
     */
    @Test
    public void customV5Excel2007ReadCustomTest() {
        try {
            UniV5Excel2007ReadService uniExcel2007Read = new UniV5Excel2007ReadService(new UniV5SheetDefaultHandler() {
                @Override
                public void startRowRead(int rowNum, int sheetIndex, String sheetName) {
                    String format = String.format("开始读取->行号【%s】工作表序号【%s】工作表名称【%s】", rowNum, sheetIndex, sheetName);
                    System.out.println("format = " + format);
                }

                @Override
                public void endRowRead(int rowNum, int sheetIndex, String sheetName) {
                    String format = String.format("结束读取->行号【%s】工作表序号【%s】工作表名称【%s】", rowNum, sheetIndex, sheetName);
                    System.out.println("format = " + format);
                }

                @Override
                public void cellRead(String cellRef, int rowNum, Integer sheetIndex, String sheetName, int colNum, UniExcelCellTypeEnum cellType, String cellValue, String fmtValue, String formula) {
                    String format = String.format("结束读取->单元格位置【%s】行号【%s】列号【%s】工作表序号【%s】工作表名称【%s】类型【%s】源数据【%s】展示值【%s】公式【%s】", cellRef, rowNum, colNum, sheetIndex, sheetName, cellType, cellValue, fmtValue, formula);
                    System.out.println("format = " + format);
                }


            });
            uniExcel2007Read.read("测试导入模拟数据.xlsx", -1);
        } catch (Exception e) {
            System.out.println("e = " + e);
        }
    }


}
