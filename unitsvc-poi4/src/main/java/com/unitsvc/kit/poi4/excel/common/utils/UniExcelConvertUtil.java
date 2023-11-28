package com.unitsvc.kit.poi4.excel.common.utils;

import com.unitsvc.kit.poi4.excel.read.service.enums.FieldFormatEnum;
import com.unitsvc.kit.poi4.excel.read.service.model.TaskFieldModel;
import com.unitsvc.kit.poi4.excel.common.model.UniExcelHeaderReq;
import com.unitsvc.kit.poi4.excel.common.enums.UniExcelCellFieldFixedEnum;
import com.unitsvc.kit.poi4.excel.common.enums.UniExcelCellFieldTypeEnum;
import com.unitsvc.kit.poi4.excel.read.service.enums.FieldTypeEnum;

/**
 * 奖金系统表头转换工具类，目的是接口兼容
 *
 * @author : jun
 * @version : v1
 * @date : 2022/10/13 16:55
 */
public class UniExcelConvertUtil {

    /**
     * 综合判断字段数据类型
     *
     * @param property        英文属性标识
     * @param fieldFormatEdit 编辑字段属性
     * @param columnType      字段属性类型
     * @return
     */
    public static UniExcelCellFieldTypeEnum transformFieldType(String property, FieldFormatEnum fieldFormatEdit, FieldTypeEnum columnType) {
        // 默认字符串类型
        UniExcelCellFieldTypeEnum result = UniExcelCellFieldTypeEnum.FIELD_STRING;
        // 综合判断数据类型
        if (null != columnType) {
            switch (columnType) {
                case DATE:
                    result = UniExcelCellFieldTypeEnum.FIELD_DATE;
                case STRING:
                    if (null != fieldFormatEdit) {
                        // 判断可编辑字段类型
                        result = checkFieldEditType(property, fieldFormatEdit);
                    }
                    break;
                case BOOLEAN:
                    // 布尔类型
                    result = UniExcelCellFieldTypeEnum.FIELD_BOOLEAN;
                    break;
                case NUMERICAL:
                    // 数值类型
                    result = UniExcelCellFieldTypeEnum.FIELD_NUMBER;
                    break;
                default:
            }
        }
        if (null == columnType) {
            // 判断可编辑字段类型
            result = checkFieldEditType(property, fieldFormatEdit);
        }
        // 默认返回字符串类型
        return result;
    }

    /**
     * 判断当前字段是否为时间标识，仅做辅助验证
     */
    private static final String TIME_NAME_FLAG = "time";

    /**
     * 判断可编辑的字段类型
     *
     * @param property        字段英文标识
     * @param fieldFormatEdit 可编辑字段
     * @return
     */
    private static UniExcelCellFieldTypeEnum checkFieldEditType(String property, FieldFormatEnum fieldFormatEdit) {
        // 默认字符串类型
        UniExcelCellFieldTypeEnum result = UniExcelCellFieldTypeEnum.FIELD_STRING;
        if (null != fieldFormatEdit) {
            switch (fieldFormatEdit) {
                case NUMERICAL:
                    // 字符串类型
                    result = UniExcelCellFieldTypeEnum.FIELD_NUMBER;
                    break;
                case DATE:
                    // 时间类型
                    result = UniExcelCellFieldTypeEnum.FIELD_DATE;
                    break;
                default:
                    // 个别时间类型字段，都为字符串类型，此处特别处理
                    if (property.toLowerCase().contains(TIME_NAME_FLAG)) {
                        // 时间类型
                        result = UniExcelCellFieldTypeEnum.FIELD_DATE;
                    }
            }
        }
        return result;
    }

    /**
     * 基于奖金任务字段参数构建动态表头
     *
     * @param taskField 奖金任务字段
     * @return
     */
    public static UniExcelHeaderReq buildDynamicHeaderFromTaskField(TaskFieldModel taskField) {
        UniExcelHeaderReq dynamicHeaderDTO = new UniExcelHeaderReq();
        // 字段唯一ID
        dynamicHeaderDTO.setFieldId(taskField.getId());
        // 字段中文名
        dynamicHeaderDTO.setNameCn(taskField.getNameCn());
        // 字段英文名
        dynamicHeaderDTO.setNameEn(taskField.getNameEn());
        // 是否可编辑
        dynamicHeaderDTO.setColumnEdit(taskField.isAbleEdit());
        // 固定类型
        dynamicHeaderDTO.setFixed(UniExcelCellFieldFixedEnum.getFixedEnum(taskField.getFixed()));
        // 字段类型
        dynamicHeaderDTO.setFieldType(transformFieldType(taskField.getNameCn(), taskField.getFieldFormat(), taskField.getType()));
        // 分组默认为空
        dynamicHeaderDTO.setGroupList(null);
        return dynamicHeaderDTO;
    }

}
