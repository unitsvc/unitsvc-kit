package com.unitsvc.kit.facade.db.antlr.d4.vistor;

/**
 * 功能描述：
 *
 * @author : jun
 * @version : v1.0.0
 * @since : 2023/10/30 10:35
 **/
public class StrUtil {

    /**
     * 特殊转义字符串处理
     *
     * @param input 输入
     * @return
     */
    public static String replaceStr(String input) {
        if (input.startsWith("'") && input.endsWith("'") || input.startsWith("\"") && input.endsWith("\"")) {
            input = input.substring(1);
            input = input.substring(0, input.length() - 1);
        }
        return input;
    }
}
