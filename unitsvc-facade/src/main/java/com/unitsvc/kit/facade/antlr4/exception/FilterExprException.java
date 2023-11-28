package com.unitsvc.kit.facade.antlr4.exception;

/**
 * 功能描述：规则表达式异常
 *
 * @author : jun
 * @version : v1.0.0
 * @since : 2023/10/31 13:49
 **/
public class FilterExprException extends RuntimeException {

    private static final long serialVersionUID = 2708701700853908387L;

    public FilterExprException() {
        super();
    }

    public FilterExprException(String message) {
        super(message);
    }

    public FilterExprException(String message, Throwable cause) {
        super(message, cause);
    }

    public FilterExprException(Throwable cause) {
        super(cause);
    }
}
