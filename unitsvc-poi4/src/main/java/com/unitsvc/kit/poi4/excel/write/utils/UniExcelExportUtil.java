package com.unitsvc.kit.poi4.excel.write.utils;

import cn.hutool.core.lang.Console;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;

import java.util.Objects;

/**
 * 功能描述：表格导出工具类
 *
 * @author : jun
 * @version : v1.0.0
 * @since : 2023/8/16 9:47
 **/
public class UniExcelExportUtil {

    /**
     * 合并工作表列单元格数据
     *
     * @param sheet     工作表
     * @param startRow  起始行，说明：从第几行开始合并，起始行为0
     * @param endColumn 结束行，说明，合并到第几列截止，起始列为0
     */
    public static void mergeCellColumn(Sheet sheet, int startRow, int endColumn) {
        // 递归处理数据
        recursiveMergeCellColumn(sheet, startRow, sheet.getLastRowNum(), 0, endColumn, false);
    }

    /**
     * 递归合并工作表列单元格数据
     *
     * @param sheet       工作表
     * @param startRow    起始行
     * @param endRow      结束行
     * @param startColumn 起始列
     * @param endColumn   结束列
     */
    public static void recursiveMergeCellColumn(Sheet sheet, int startRow, int endRow, int startColumn, int endColumn, boolean debug) {
        int firstRow = startRow;
        int lastRow = startRow;
        // 上一次比较是否相同
        boolean isPrevCompareSame = false;
        String prevMergeAddress = null;
        // 从第几行开始判断是否相同
        String currentMergeAddress;
        if (startRow <= endRow) {
            for (int rowIndex = startRow; rowIndex < endRow; rowIndex++) {
                Row curRow = sheet.getRow(rowIndex);
                if (null == curRow) {
                    // 说明：一旦出现单元格为null，一般为行数据写入磁盘，导致sheet无法读取。
                    continue;
                }
                // 当前行单元格
                Cell curRowCell = curRow.getCell(startColumn);
                // 获取当前行数据
                String curRowCellContent = getValueAsString(curRowCell);
                // 获取下一行数据
                String nextRowCellContent = getValueAsString(sheet.getRow(rowIndex + 1).getCell(startColumn));
                if (curRowCellContent.equals(nextRowCellContent)) {
                    if (!isPrevCompareSame) {
                        firstRow = rowIndex;
                    }
                    lastRow = rowIndex + 1;
                    isPrevCompareSame = true;
                } else {
                    currentMergeAddress = String.format("%s,%s,%s,%s", firstRow, lastRow, startColumn, startColumn);
                    if (lastRow > firstRow && !Objects.equals(currentMergeAddress, prevMergeAddress)) {
                        if (firstRow >= startRow) {
                            if (debug) {
                                Console.log("row[{},{}],column[{}]-中间行", firstRow, lastRow, startColumn);
                            }
                            // 合并单元格
                            sheet.addMergedRegion(new CellRangeAddress(firstRow, lastRow, startColumn, startColumn));
                            prevMergeAddress = currentMergeAddress;
                            // 获取单元格样式
                            CellStyle cellStyle = curRowCell.getCellStyle();
                            if (null != cellStyle) {
                                // 合并后的单元格上下居中
                                cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
                            }
                            // ------------------ 递归判断下个节点是否允许合并 ----------------------------
                            int nextStartColumn = startColumn + 1;
                            if (nextStartColumn <= endColumn && isPrevCompareSame) {
                                recursiveMergeCellColumn(sheet, firstRow, lastRow, nextStartColumn, endColumn, debug);
                            }
                        }
                    }
                    isPrevCompareSame = false;
                }
                // 最后一行时判断是否有需要合并的行
                if ((rowIndex == endRow - 1) && (lastRow > firstRow)) {
                    currentMergeAddress = String.format("%s,%s,%s,%s", firstRow, lastRow, startColumn, startColumn);
                    if (!Objects.equals(currentMergeAddress, prevMergeAddress)) {
                        if (firstRow >= startRow) {
                            if (debug) {
                                Console.log("row[{},{}],column[{}]-结束行", firstRow, lastRow, startColumn);
                            }
                            // 合并单元格
                            sheet.addMergedRegion(new CellRangeAddress(firstRow, lastRow, startColumn, startColumn));
                            // 获取单元格样式
                            CellStyle cellStyle = curRowCell.getCellStyle();
                            if (null != cellStyle) {
                                // 合并后的单元格上下居中
                                cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
                            }
                            // ------------------ 递归判断下个节点是否允许合并 ----------------------------
                            int nextStartColumn = startColumn + 1;
                            if (nextStartColumn <= endColumn && isPrevCompareSame) {
                                recursiveMergeCellColumn(sheet, firstRow, lastRow, nextStartColumn, endColumn, debug);
                            }
                        }
                    }
                    isPrevCompareSame = false;
                }
            }
        }
    }

    /**
     * 获取单元格值
     * <p>
     * 说明：转换为字符串
     *
     * @param cell 单元格
     * @return
     */
    private static String getValueAsString(Cell cell) {
        if (cell == null) {
            return "";
        }
        CellType cellType = cell.getCellType();
        if (cellType == CellType.STRING) {
            return cell.getStringCellValue();
        } else if (cellType == CellType.NUMERIC) {
            return String.valueOf(cell.getNumericCellValue());
        } else if (cellType == CellType.BOOLEAN) {
            return String.valueOf(cell.getBooleanCellValue());
        } else {
            return "";
        }
    }

}