package com.unitsvc.kit.facade.script.exception;

/**
 * 功能描述：编译异常
 *
 * @author : jun
 * @version : v1.0.0
 * @since : 2023/5/24 13:49
 **/
public class JsCompileException extends RuntimeException {

    private static final long serialVersionUID = 2719350686173234108L;

    public JsCompileException() {
        super();
    }

    public JsCompileException(String message) {
        super(message);
    }

    public JsCompileException(String message, Throwable cause) {
        super(message, cause);
    }

    public JsCompileException(Throwable cause) {
        super(cause);
    }

}
