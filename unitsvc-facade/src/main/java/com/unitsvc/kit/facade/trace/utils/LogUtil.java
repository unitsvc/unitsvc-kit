package com.unitsvc.kit.facade.trace.utils;

/**
 * 功能描述：
 *
 * @author : jun
 * @version : v1.0.0
 * @since : 2023/9/28 15:16
 **/
public class LogUtil {

    /**
     * 获取首行异常日志
     *
     * @param e 异常
     * @return
     */
    public static String exceptionFirstLineStr(Exception e) {
        StringBuilder sb = new StringBuilder();
        if (null != e) {
            try {
                // 获取异常的堆栈跟踪
                StackTraceElement[] stackTrace = e.getStackTrace();
                sb.append(String.format("%s: %s", e.getClass().getName(), e.getMessage()));
                // 将第一行堆栈元素转换为字符串
                if (stackTrace.length > 0) {
                    sb.append("\n");
                    sb.append(String.format("\t%s", stackTrace[0].toString()));
                }
            } catch (Exception ex) {
                // pass
                return String.format("%s", e.getMessage());
            }
        }
        return sb.toString();
    }

    /**
     * 获取首行异常日志
     *
     * @param e 异常
     * @return
     */
    public static String exceptionFirstLineStr(Throwable e) {
        StringBuilder sb = new StringBuilder();
        if (null != e) {
            try {
                // 获取异常的堆栈跟踪
                StackTraceElement[] stackTrace = e.getStackTrace();
                sb.append(String.format("%s: %s", e.getClass().getName(), e.getMessage()));
                // 将第一行堆栈元素转换为字符串
                if (stackTrace.length > 0) {
                    sb.append("\n");
                    sb.append(String.format("\t%s", stackTrace[0].toString()));
                }
            } catch (Exception ex) {
                // pass
                return String.format("%s", e.getMessage());
            }
        }
        return sb.toString();
    }

}
