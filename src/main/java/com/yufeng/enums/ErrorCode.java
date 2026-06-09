package com.yufeng.enums;

/**
 * 错误状态码
 *
 * @author yufeng
 * @since 2025/5/9 9:04
 */
public enum ErrorCode {
    SUCCESS(0,"success"),
    PARAMS_ERROR(4000,"请求参数错误"),
    NULL_ERROR(4001,"请求参数为空"),
    NO_LOGIN_ERROR(40100,"未登录"),
    NO_AUTH_ERROR(40101,"无权限"),
    SYSTEM_ERROR(500,"系统异常");
    private Integer code;
    private String message;
    private String description;

    ErrorCode(Integer code, String message, String description) {
        this.code = code;
        this.message = message;
        this.description = description;
    }

    ErrorCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getDescription() {
        return description;
    }
}
