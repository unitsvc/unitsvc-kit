package com.unitsvc.kit.facade.script.exception;

/**
 * 功能描述：计算异常
 *
 * @author : jun
 * @version : v1.0.0
 * @since : 2023/5/24 13:41
 **/
public class JsCalculateException extends RuntimeException {

    private static final long serialVersionUID = -5058214877201318467L;

    public JsCalculateException() {
        super();
    }

    public JsCalculateException(String message) {
        super(message);
    }

    public JsCalculateException(String message, Throwable cause) {
        super(message, cause);
    }

    public JsCalculateException(Throwable cause) {
        super(cause);
    }
}
