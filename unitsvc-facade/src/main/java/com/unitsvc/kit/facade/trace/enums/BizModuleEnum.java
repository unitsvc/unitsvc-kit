package com.unitsvc.kit.facade.trace.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 功能描述：业务类型枚举
 *
 * @author : jun
 * @version : v1.0.0
 * @date : 2023/3/3 18:46
 **/
@Getter
@AllArgsConstructor
public enum BizModuleEnum {

    /**
     * 默认
     */
    DEFAULT("默认"),

    ;

    /**
     * 中文名称
     */
    private final String nameCn;

    /**
     * 获取业务类型枚举
     *
     * @param bizType 业务类型
     * @return
     */
    public static BizModuleEnum getBizTypeEnum(String bizType) {
        for (BizModuleEnum value : BizModuleEnum.values()) {
            if (value.name().equals(bizType)) {
                return value;
            }
        }
        return null;
    }

}
