package com.yufeng.util;

import com.yufeng.domain.response.BaseResponse;
import com.yufeng.enums.ErrorCode;

/**
 * 返回时更清晰地看到是成功还是失败
 *
 * @author yufeng
 * @since 2025/5/9 9:30
 */

//失败，在全局处理器中被调用
//成功，在业务层被调用
public class ResultUtil <T> {
    public static<T> BaseResponse<T> success(T data){
        return new BaseResponse<>(ErrorCode.SUCCESS,data);
    }

    //系统异常
    public static BaseResponse error(ErrorCode errorCode){
        return new BaseResponse<>(errorCode);
    }

    public static BaseResponse error(ErrorCode errorCode,String description){
        return new BaseResponse<>(errorCode,description);
    }

    //业务异常
    public static BaseResponse error(Integer code,String message,String description){
        return new BaseResponse<>(code,message,description);
    }
    public static BaseResponse error(Integer code,String message){
        return new BaseResponse<>(code,message);
    }
}
