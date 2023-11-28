package com.unitsvc.kit.poi4.excel;

import cn.hutool.core.io.FileUtil;
import com.unitsvc.kit.poi4.excel.write.utils.UniExcelExportUtil;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class UniMergeTest {

    /**
     * 合并单元格测试类
     *
     * @throws IOException
     */
    @Test
    public void mergeExcelColumnTest() throws IOException {
        // 获取文件路径
        File file = FileUtil.file("./input.xlsx");
        FileInputStream inputStream = new FileInputStream(file);
        Workbook workbook = new XSSFWorkbook(inputStream);
        // 选择要操作的工作表
        Sheet sheet = workbook.getSheetAt(0);
        // 合并单元格
        UniExcelExportUtil.mergeCellColumn(sheet, 2, 3);
        // 获取文件路径
        FileOutputStream outputStream = new FileOutputStream("./output.xlsx");
        workbook.write(outputStream);
        outputStream.close();
        System.out.println("表格合并完成");
    }

}
