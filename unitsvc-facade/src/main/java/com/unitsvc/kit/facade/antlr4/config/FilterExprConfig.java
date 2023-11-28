package com.unitsvc.kit.facade.antlr4.config;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 功能描述：规则表达式配置
 *
 * @author : jun
 * @version : v1.0.0
 * @since : 2023/10/31 14:12
 **/
@Accessors(chain = true)
@Data
public class FilterExprConfig implements Serializable {

    private static final long serialVersionUID = -1779670931175158806L;

    /**
     * 可选，是否忽略like语句空指针异常
     * <p>
     * 说明：若不忽略，则用“null”字符串代替，推荐false捕获异常，保证规则正确
     */
    private Boolean ignoreLikeNullException = false;

    /**
     * 可选，是否忽略正则异常
     * <p>
     * 说明：若不忽略，则可能导致规则解析不完整，推荐false捕获异常，保证规则正确
     */
    private Boolean ignoreSyntaxException = false;

}
