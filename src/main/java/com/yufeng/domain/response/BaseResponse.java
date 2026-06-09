package com.yufeng.domain.response;

import com.yufeng.enums.ErrorCode;
import lombok.Data;

/**
 * 通用返回对象
 *
 * @author yufeng
 * @since 2025/5/9 9:15
 */
@Data
public class BaseResponse <T> {
    private Integer code;
    private String message;
    private String description;
    private T data;

    //便于后面的构造函数的声明
    public BaseResponse(Integer code, String message, String description, T data) {
        this.code = code;
        this.message = message;
        this.description = description;
        this.data = data;
    }



    //成功用
    public BaseResponse(ErrorCode errorCode, T data) {
        this(errorCode.getCode(),errorCode.getMessage()," ",data);
    }

    //比较一般的情况
    public BaseResponse(Integer code, String message, T data) {
        this(code,message,"",data);
    }

    //失败用
    public BaseResponse(ErrorCode errorCode){
        this(errorCode.getCode(),errorCode.getMessage(),null);
    }
    public BaseResponse(ErrorCode errorCode,String description){
        this(errorCode.getCode(),errorCode.getMessage(),description,null);
    }
    public BaseResponse(Integer code, String message) {
        this(code,message,null);
    }
}
