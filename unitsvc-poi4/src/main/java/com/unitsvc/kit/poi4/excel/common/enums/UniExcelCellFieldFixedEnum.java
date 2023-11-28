package com.unitsvc.kit.poi4.excel.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang.StringUtils;

/**
 * 功能描述：导入导出字段展示顺序排序枚举
 *
 * @author : jun
 * @version : v1.0.0
 * @date : 2022/10/17 20:15
 */
@Getter
@AllArgsConstructor
public enum UniExcelCellFieldFixedEnum {

    /**
     * 字段左边固定
     */
    LEFT("left", "左边固定"),

    /**
     * 字段右边固定
     */
    RIGHT("right", "右边固定"),

    /**
     * 默认
     */
    DEFAULT("default", "默认");

    /**
     * 字段类型
     */
    private final String type;

    /**
     * 字段描述
     */
    private final String desc;

    /**
     * 获取指定固定类型枚举
     *
     * @param fixedType 固定类型值
     * @return 返回固定类型枚举
     */
    public static UniExcelCellFieldFixedEnum getFixedEnum(String fixedType) {
        if (StringUtils.isEmpty(fixedType)) {
            return DEFAULT;
        }
        for (UniExcelCellFieldFixedEnum uniExcelCellFieldFixedEnum : UniExcelCellFieldFixedEnum.values()) {
            if (uniExcelCellFieldFixedEnum.getType().equals(fixedType)) {
                return uniExcelCellFieldFixedEnum;
            }
        }
        return DEFAULT;
    }
}
