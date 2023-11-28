package com.unitsvc.kit.core.excel.exception;

/**
 * 功能描述：表格读取异常
 *
 * @author : jun
 * @version : v1.0.0
 * @date : 2023/2/23 13:28
 **/
public class UniExcelReadException extends RuntimeException {

    private static final long serialVersionUID = -4860498766348422943L;

    public UniExcelReadException() {
        super();
    }

    public UniExcelReadException(String message) {
        super(message);
    }

    public UniExcelReadException(String message, Throwable cause) {
        super(message, cause);
    }

}
