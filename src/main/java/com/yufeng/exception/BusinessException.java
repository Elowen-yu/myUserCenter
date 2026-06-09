package com.yufeng.exception;

import com.yufeng.enums.ErrorCode;

/**
 * 业务异常类
 *
 * @author yufeng
 * @since 2025/5/9 9:42
 */
public class BusinessException extends RuntimeException{
    private String description;
    private Integer code;

    public BusinessException(ErrorCode errorCode, String description) {
        super(errorCode.getMessage());
        this.description = description;
        this.code = errorCode.getCode();
    }
    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.code=errorCode.getCode();
    }

    public String getDescription() {
        return description;
    }

    public Integer getCode() {
        return code;
    }
}
