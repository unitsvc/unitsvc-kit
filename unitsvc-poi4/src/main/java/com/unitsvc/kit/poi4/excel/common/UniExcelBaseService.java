package com.unitsvc.kit.poi4.excel.common;

import cn.hutool.core.util.StrUtil;
import com.unitsvc.kit.poi4.excel.common.enums.UniExcelCellAttrLabelEnum;
import com.unitsvc.kit.poi4.excel.common.enums.UniExcelCellFieldFixedEnum;
import com.unitsvc.kit.poi4.excel.common.enums.UniExcelCellFieldTypeEnum;
import com.unitsvc.kit.poi4.excel.common.model.UniExcelHeaderReq;
import com.unitsvc.kit.poi4.excel.read.utils.UniExcelCellDateUtil;
import com.unitsvc.kit.poi4.excel.write.config.UniExcelStyleExportConfig;
import com.unitsvc.kit.poi4.excel.write.style.IUniExcelCustomStyle;
import com.unitsvc.kit.poi4.excel.write.utils.UniExcelCellStyleUtil;
import lombok.Builder;
import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.compress.utils.Lists;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddressList;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;

/**
 * 功能描述：通用导入导出通用方法
 *
 * @author : jun
 * @version : v1.0.0
 * @date : 2022/10/18 12:40
 */
public class UniExcelBaseService {

    /**
     * 设置单元格下拉选项，有严格的限制条件。备注：此方法仅仅使用与简单的下拉选择框。
     * String[] downValueList = {"200.001", "10", "文本", "2022-10-26", "012345", "否"};
     * this.setCellDownSelectValueListByRestrict(sheet, "下拉框", 3, 65535, 1, 10, downValueList);
     *
     * @param sheet         工作表
     * @param headerName    表头列名称
     * @param startRow      数据范围起始行
     * @param endRow        数据范围结束行
     * @param startCol      数据范围起始列
     * @param endCol        数据范围结束列
     * @param downValueList 下拉选项，实际测试，中文：128下拉+384个字节。数字：88下拉+166个字节。英文+数字：66下拉+188字节。中文+数字：66下拉+320个字节。中文+数字+英文：54下拉+308个字节。
     */
    public void setCellDownSelectValueListByRestrict(Sheet sheet, String headerName, int startRow, int endRow, int startCol, int endCol, String[] downValueList) {
        // 设置验证生效的范围
        CellRangeAddressList addressList = new CellRangeAddressList(startRow, endRow, startCol, endCol);
        // 创建数据验证类
        DataValidationHelper helper = sheet.getDataValidationHelper();
        // 加载下拉列表内容
        DataValidationConstraint constraint = helper.createExplicitListConstraint(downValueList);
        // 创建数据验证类
        DataValidation validation = helper.createValidation(constraint, addressList);
        // 只能从下拉菜单中选值
        validation.setSuppressDropDownArrow(true);
        // 设置错误提示
        validation.createErrorBox("温馨提示", String.format("%s，请从指定的下拉列表中选取值！！！", headerName));
        // 设置错误警告框
        validation.setShowErrorBox(true);
        // 验证和工作簿绑定
        sheet.addValidationData(validation);
    }

    /**
     * 创建所有列单元格数据类型
     *
     * @param workbook                  工作簿
     * @param realDynamicDataHeaderList 动态表头列
     * @param uniExcelCustomStyle       自定义样式类型
     * @param uniExcelStyleExportConfig 导出配置
     * @return
     */
    public Map<UniExcelHeaderReq, UniCellDataCellStyle> createAllColumnCellType(Workbook workbook, List<UniExcelHeaderReq> realDynamicDataHeaderList, IUniExcelCustomStyle uniExcelCustomStyle, UniExcelStyleExportConfig uniExcelStyleExportConfig) {
        // 创建数据类型格式化
        DataFormat dataFormat = workbook.createDataFormat();
        // 定义所有数据类型枚举
        Map<UniExcelHeaderReq, UniCellDataCellStyle> cellTypeMap = new HashMap<>(32);
        // 创建所有列单元格类型
        for (UniExcelHeaderReq uniExcelHeaderReq : realDynamicDataHeaderList) {
            // 创建单元格数据类型
            UniCellDataCellStyle uniCellDataCellStyle = this.createUniCellDataCellStyle(uniExcelHeaderReq, dataFormat, workbook, uniExcelCustomStyle, uniExcelStyleExportConfig);
            // 保存单元格数据类型
            cellTypeMap.put(uniExcelHeaderReq, uniCellDataCellStyle);
        }
        return cellTypeMap;
    }

    /**
     * 创建列单元格样式，包含可编辑、不可编辑
     *
     * @param dynamicHeaderDTO          动态表头字段
     * @param dataFormat                数据格式化
     * @param workbook                  工作簿
     * @param uniExcelCustomStyle       自定义样式
     * @param uniExcelStyleExportConfig 导出配置
     * @return
     */
    private UniCellDataCellStyle createUniCellDataCellStyle(UniExcelHeaderReq dynamicHeaderDTO, DataFormat dataFormat, Workbook workbook, IUniExcelCustomStyle uniExcelCustomStyle, UniExcelStyleExportConfig uniExcelStyleExportConfig) {
        // 创建可编辑单元格样式
        CellStyle cellStyleEdit = workbook.createCellStyle();
        // 创建可编辑字体样式
        Font cellFontEdit = workbook.createFont();
        // 设置数据单元格编辑类型
        uniExcelCustomStyle.setDataCellEditStyle(cellFontEdit, cellStyleEdit);

        // 创建只读的单元格样式
        CellStyle cellStyleRead = workbook.createCellStyle();
        // 创建可编辑的字体样式
        Font cellFontRead = workbook.createFont();
        // 设置数据单元格编辑类型
        uniExcelCustomStyle.setDateCellReadonlyStyle(cellFontRead, cellStyleRead);

        // 创建可编辑单元格样式，汇总行
        CellStyle cellStyleEditSummaryMark = workbook.createCellStyle();
        // 创建可编辑字体样式，汇总行
        Font cellFontEditSummaryMark = workbook.createFont();
        // 设置数据单元格编辑类型，汇总行
        uniExcelCustomStyle.setDataCellEditSummaryMarkStyle(cellFontEditSummaryMark, cellStyleEditSummaryMark);

        // 创建只读的单元格样式，汇总行
        CellStyle cellStyleReadSummaryMark = workbook.createCellStyle();
        // 创建可编辑的字体样式，汇总行
        Font cellFontReadSummaryMark = workbook.createFont();
        // 设置数据单元格编辑类型，汇总行
        uniExcelCustomStyle.setDateCellReadonlySummaryMarkStyle(cellFontReadSummaryMark, cellStyleReadSummaryMark);

        // 获取单元格类型
        UniExcelCellFieldTypeEnum dynamicFieldTypeEnum = dynamicHeaderDTO.getFieldType();
        // 判断数据类型，调整单元格样式
        switch (dynamicFieldTypeEnum) {
            case FIELD_STRING:
                // 字符串
                // 备注：可编辑的字符串必须设置为文本类型，而不应该是常规，否则比如工号修改后会变成数字类型。不可编辑的样式，默认常规。
                UniExcelCellStyleUtil.setCellTypeString(dataFormat, cellStyleEdit);
                UniExcelCellStyleUtil.setCellTypeString(dataFormat, cellStyleEditSummaryMark);

                // 获取表头展示标签
                List<UniExcelCellAttrLabelEnum> showLabels = dynamicHeaderDTO.getShowLabels();
                // 处理标签
                if (CollectionUtils.isNotEmpty(showLabels) && showLabels.contains(UniExcelCellAttrLabelEnum.ATTR_TEXT)) {
                    // 若包含文本类型标签，则单元格格式强制为文本类型
                    UniExcelCellStyleUtil.setCellTypeString(dataFormat, cellStyleEdit);
                    UniExcelCellStyleUtil.setCellTypeString(dataFormat, cellStyleRead);

                    UniExcelCellStyleUtil.setCellTypeString(dataFormat, cellStyleEditSummaryMark);
                    UniExcelCellStyleUtil.setCellTypeString(dataFormat, cellStyleReadSummaryMark);
                }
                break;
            case FIELD_NUMBER:
                // 数字类型，默认处理成小数，保留N位小数，备注：仅展示，实际精度还在
                UniExcelCellStyleUtil.setCellTypeFloat(dataFormat, cellStyleEdit, dynamicHeaderDTO, uniExcelStyleExportConfig);
                UniExcelCellStyleUtil.setCellTypeFloat(dataFormat, cellStyleRead, dynamicHeaderDTO, uniExcelStyleExportConfig);

                UniExcelCellStyleUtil.setCellTypeFloat(dataFormat, cellStyleEditSummaryMark, dynamicHeaderDTO, uniExcelStyleExportConfig);
                UniExcelCellStyleUtil.setCellTypeFloat(dataFormat, cellStyleReadSummaryMark, dynamicHeaderDTO, uniExcelStyleExportConfig);
                break;
            case FIELD_INTEGER:
                // 整数类型，备注：整数类型不在保留小数精度否则会出现歧义
                UniExcelCellStyleUtil.setCellTypeInteger(dataFormat, cellStyleEdit);
                UniExcelCellStyleUtil.setCellTypeInteger(dataFormat, cellStyleRead);

                UniExcelCellStyleUtil.setCellTypeInteger(dataFormat, cellStyleEditSummaryMark);
                UniExcelCellStyleUtil.setCellTypeInteger(dataFormat, cellStyleReadSummaryMark);
                break;
            case FIELD_DOUBLE:
                // 小数
                UniExcelCellStyleUtil.setCellTypeDouble(dataFormat, cellStyleEdit);
                UniExcelCellStyleUtil.setCellTypeDouble(dataFormat, cellStyleRead);

                // 小数
                UniExcelCellStyleUtil.setCellTypeDouble(dataFormat, cellStyleEditSummaryMark);
                UniExcelCellStyleUtil.setCellTypeDouble(dataFormat, cellStyleReadSummaryMark);
                break;
            case FIELD_BOOLEAN:
                // 布尔，备注：根据具体数值调整
                // 设置单元格居中对齐
                UniExcelCellStyleUtil.setCellCenterStyle(cellStyleEdit);
                UniExcelCellStyleUtil.setCellCenterStyle(cellStyleRead);

                UniExcelCellStyleUtil.setCellCenterStyle(cellStyleEditSummaryMark);
                UniExcelCellStyleUtil.setCellCenterStyle(cellStyleReadSummaryMark);
                break;
            case FIELD_DATE:
                // 时间类型
                UniExcelCellStyleUtil.setCellTypeCustomDate(dataFormat, cellStyleEdit, dynamicHeaderDTO, uniExcelStyleExportConfig);
                UniExcelCellStyleUtil.setCellTypeCustomDate(dataFormat, cellStyleRead, dynamicHeaderDTO, uniExcelStyleExportConfig);

                UniExcelCellStyleUtil.setCellTypeCustomDate(dataFormat, cellStyleEditSummaryMark, dynamicHeaderDTO, uniExcelStyleExportConfig);
                UniExcelCellStyleUtil.setCellTypeCustomDate(dataFormat, cellStyleReadSummaryMark, dynamicHeaderDTO, uniExcelStyleExportConfig);
                break;
            case FIELD_FORMULA:
                // 公式类型
                break;
            default:
        }
        return UniCellDataCellStyle.builder()
                .cellDataAllowEditStyle(cellStyleEdit)
                .cellDataReadonlyStyle(cellStyleRead)
                .cellDataAllowEditSummaryMarkStyle(cellStyleEditSummaryMark)
                .cellDataReadonlySummaryMarkStyle(cellStyleReadSummaryMark)
                .dynamicFieldTypeEnum(dynamicFieldTypeEnum)
                .dataFormat(dataFormat).build();
    }

    /**
     * 递归获取表头数据字段
     *
     * @param uniExcelHeaderReqList 字段列表
     * @return
     */
    protected List<UniExcelHeaderReq> findDataHeaderList(List<UniExcelHeaderReq> uniExcelHeaderReqList) {
        List<UniExcelHeaderReq> dataHeaderList = Lists.newArrayList();
        if (CollectionUtils.isEmpty(uniExcelHeaderReqList)) {
            return dataHeaderList;
        }
        for (UniExcelHeaderReq uniExcelHeaderReq : uniExcelHeaderReqList) {
            List<UniExcelHeaderReq> groupList = uniExcelHeaderReq.getGroupList();
            if (CollectionUtils.isNotEmpty(groupList)) {
                // 递归获取表头数据
                dataHeaderList.addAll(this.findDataHeaderList(groupList));
            } else {
                dataHeaderList.add(uniExcelHeaderReq);
            }
        }
        return dataHeaderList;
    }

    /**
     * 获取最大表头深度
     *
     * <pre>
     *  备注：2023-08-17 17:21 新增
     *  说明；用于计算表头深度，合并动态表头
     * </pre>
     *
     * @param uniExcelHeaderReqList 字段列表
     * @param curDeep               当前深度
     * @return 数据列最大深度
     */
    protected int calcMaxDataHeaderDeep(List<UniExcelHeaderReq> uniExcelHeaderReqList, int curDeep) {
        int maxDeep = curDeep;
        if (CollectionUtils.isEmpty(uniExcelHeaderReqList)) {
            return curDeep;
        }
        int tempDeep = curDeep + 1;
        for (UniExcelHeaderReq uniExcelHeaderReq : uniExcelHeaderReqList) {
            List<UniExcelHeaderReq> groupList = uniExcelHeaderReq.getGroupList();
            if (CollectionUtils.isNotEmpty(groupList)) {
                int nextDeep = this.calcMaxDataHeaderDeep(groupList, tempDeep);
                if (nextDeep > maxDeep) {
                    maxDeep = nextDeep;
                }
            }
        }
        return maxDeep;
    }

    /**
     * 计算分组表头跨度
     *
     * @param uniExcelHeaderReq 动态表头
     * @return
     */
    protected int calcGroupDataHeaderSpan(UniExcelHeaderReq uniExcelHeaderReq) {
        if (null == uniExcelHeaderReq) {
            return 0;
        }
        int sumSpan = 0;
        // 嵌套分组
        List<UniExcelHeaderReq> groupList = uniExcelHeaderReq.getGroupList();
        if (CollectionUtils.isNotEmpty(groupList)) {
            for (UniExcelHeaderReq excelHeaderReq : groupList) {
                // 递归计算跨度
                sumSpan = sumSpan + calcGroupDataHeaderSpan(excelHeaderReq);
            }
        } else {
            return 1;
        }
        return sumSpan;
    }

    /**
     * 定义单元格类型
     */
    @Data
    @Builder
    public static class UniCellDataCellStyle implements Serializable {

        private static final long serialVersionUID = 4147712848387629103L;

        /**
         * 单元格数据可编辑类型
         */
        private CellStyle cellDataAllowEditStyle;

        /**
         * 单元格数据只读类型
         */
        private CellStyle cellDataReadonlyStyle;

        /**
         * 单元格数据可编辑类型，汇总行
         */
        private CellStyle cellDataAllowEditSummaryMarkStyle;

        /**
         * 单元格数据只读类型，汇总行
         */
        private CellStyle cellDataReadonlySummaryMarkStyle;

        /**
         * 动态字段类型
         */
        private UniExcelCellFieldTypeEnum dynamicFieldTypeEnum;

        /**
         * 单元格数据格式化
         */
        private DataFormat dataFormat;

        /**
         * 设置单元格数据
         *
         * @param cell                      单元格
         * @param cellValue                 单元格值
         * @param dynamicHeaderDTO          表头信息
         * @param uniExcelStyleExportConfig 导出配置
         */
        @SuppressWarnings("all")
        public void setCellValue(Cell cell, String cellValue, UniExcelHeaderReq dynamicHeaderDTO, UniExcelStyleExportConfig uniExcelStyleExportConfig) {
            // 若是敏感字段，则不导出该字段值
            if (Boolean.TRUE.equals(dynamicHeaderDTO.getSensitive())) {
                if (StringUtils.isNotEmpty(dynamicHeaderDTO.getCompareSensitiveText())) {
                    // 若是敏感字段，且敏感字段展示内容值等于数据值，则展示敏感字段，否则展示敏感字段原始值内容
                    if (StrUtil.equals(cellValue, dynamicHeaderDTO.getCompareSensitiveText())) {
                        cell.setCellValue(dynamicHeaderDTO.getCompareSensitiveText());
                        return;
                    }
                }
            }
            if (StringUtils.isNotEmpty(cellValue)) {
                switch (dynamicFieldTypeEnum) {
                    case FIELD_INTEGER:
                        // 整数
                        cell.setCellValue(new BigDecimal(cellValue).intValue());
                        break;
                    case FIELD_DOUBLE:
                        // 小数
                        cell.setCellValue(Double.parseDouble(cellValue));
                        break;
                    case FIELD_NUMBER:
                        // 数字类型，默认处理成小数
                        cell.setCellValue(Double.parseDouble(cellValue));
                        break;
                    case FIELD_DATE:
                        // 时间
                        cell.setCellValue(UniExcelCellDateUtil.handleTime(cellValue));
                        break;
                    case FIELD_BOOLEAN:
                        // 布尔
                        String value = UniExcelCellStyleUtil.formatCellTypeBooleanValue(cellValue, dynamicHeaderDTO, uniExcelStyleExportConfig);
                        cell.setCellValue(value);
                        break;
                    case FIELD_FORMULA:
                        // 公式
                        if (StringUtils.isNotEmpty(cellValue)) {
                            cell.setCellFormula(cellValue);
                        }
                        break;
                    case FIELD_STRING:
                        cell.setCellValue(cellValue);
                        break;
                    default:
                        cell.setCellValue(cellValue);
                }
            } else {
                // 无数据则设置为空
                cell.setBlank();
            }
        }
    }

    /**
     * 处理动态表头字段排序
     *
     * @param dynamicHeaderDTOList 动态表头字段集合
     * @return
     */
    public List<UniExcelHeaderReq> sortHeaderDTOList(List<UniExcelHeaderReq> dynamicHeaderDTOList) {
        if (null == dynamicHeaderDTOList) {
            return Collections.emptyList();
        }
        // *********************************** 处理分组字段 ***********************************

        // 定义分组字段容器
        List<UniExcelHeaderReq> groupFieldList = new LinkedList<>();
        // 定义左对齐容器
        List<UniExcelHeaderReq> leftFieldList = new LinkedList<>();
        // 定义右对齐容器
        List<UniExcelHeaderReq> rightFieldList = new LinkedList<>();

        // 移除不显示字段
        for (int i = 0; i < dynamicHeaderDTOList.size(); i++) {
            UniExcelHeaderReq dynamicHeaderDTO = dynamicHeaderDTOList.get(i);
            if (null != dynamicHeaderDTO.getGroupList() && dynamicHeaderDTO.getGroupList().size() > 0) {
                int index = i--;
                // 添加分组
                groupFieldList.add(dynamicHeaderDTO);
                // 移除分组
                dynamicHeaderDTOList.remove(index);
            }
        }

        // ************************************* 字段排序逻辑 *************************************

        if (!dynamicHeaderDTOList.isEmpty()) {
            dynamicHeaderDTOList.forEach(field -> {
                if (null != field.getFixed()) {
                    UniExcelCellFieldFixedEnum fixed = field.getFixed();
                    switch (fixed) {
                        case LEFT:
                            // 左对齐
                            leftFieldList.add(field);
                            break;
                        case RIGHT:
                            // 右对齐
                            rightFieldList.add(field);
                            break;
                        case DEFAULT:
                        default:
                    }
                }
            });
            // 移除特殊对齐字段
            dynamicHeaderDTOList.removeAll(rightFieldList);
            dynamicHeaderDTOList.removeAll(leftFieldList);
        }
        // 定义字段结果容器
        List<UniExcelHeaderReq> sortHeaderFieldList = new LinkedList<>();
        if (!leftFieldList.isEmpty()) {
            // 左对齐
            sortHeaderFieldList.addAll(leftFieldList);
        }
        if (!dynamicHeaderDTOList.isEmpty()) {
            // 普通字段
            sortHeaderFieldList.addAll(dynamicHeaderDTOList);
        }
        if (!groupFieldList.isEmpty()) {
            // 分组字段
            sortHeaderFieldList.addAll(groupFieldList);
        }
        if (!rightFieldList.isEmpty()) {
            // 右边对齐
            sortHeaderFieldList.addAll(rightFieldList);
        }

        return sortHeaderFieldList;
    }

}
