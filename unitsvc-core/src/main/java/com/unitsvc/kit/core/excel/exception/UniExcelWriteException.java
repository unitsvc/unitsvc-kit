package com.unitsvc.kit.core.excel.exception;

/**
 * 功能描述：表格写入异常
 *
 * @author : jun
 * @version : v1.0.0
 * @date : 2023/2/23 13:28
 **/
public class UniExcelWriteException extends RuntimeException {

    private static final long serialVersionUID = -4860498766348422943L;

    public UniExcelWriteException() {
        super();
    }

    public UniExcelWriteException(String message) {
        super(message);
    }

    public UniExcelWriteException(String message, Throwable cause) {
        super(message, cause);
    }

}
