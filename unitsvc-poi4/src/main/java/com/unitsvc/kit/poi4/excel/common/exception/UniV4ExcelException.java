package com.unitsvc.kit.poi4.excel.common.exception;

import lombok.Data;

/**
 * 功能描述：通用导入单元格数据异常
 *
 * @author : jun
 * @version : v1.0.0
 * @date : 2023/1/3 16:05
 **/
@Data
public class UniV4ExcelException extends RuntimeException {

    private static final long serialVersionUID = -642143581141871589L;

    public UniV4ExcelException() {
        super();
    }

    public UniV4ExcelException(String message) {
        super(message);
    }

    public UniV4ExcelException(String message, Throwable cause) {
        super(message, cause);
    }

}
