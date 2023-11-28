package com.unitsvc.kit.facade.antlr4.utils;

/**
 * 功能描述：Antlr4字符串处理工具
 *
 * @author : jun
 * @version : v1.0.0
 * @since : 2023/10/30 10:35
 **/
public class Antlr4Util {

    /**
     * 单引号
     */
    private static final String SINGLE_QUOTE = "'";

    /**
     * 双引号
     */
    private static final String DOUBLE_QUOTE = "\"";

    /**
     * 特殊转义字符串处理
     * <p>
     * 示例：“‘str’”->str,"\"str\""->str,"str"->str
     *
     * @param input 输入
     * @return
     */
    public static String replaceStartAndEndStr(String input) {
        if (input.startsWith(SINGLE_QUOTE) && input.endsWith(SINGLE_QUOTE)) {
            input = input.substring(1);
            input = input.substring(0, input.length() - 1);
        } else if (input.startsWith(DOUBLE_QUOTE) && input.endsWith(DOUBLE_QUOTE)) {
            input = input.substring(1);
            input = input.substring(0, input.length() - 1);
        }
        return input;
    }
}
