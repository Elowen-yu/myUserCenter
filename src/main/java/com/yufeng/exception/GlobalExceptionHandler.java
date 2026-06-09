package com.yufeng.exception;

import com.yufeng.domain.response.BaseResponse;
import com.yufeng.enums.ErrorCode;
import com.yufeng.util.ResultUtil;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器
 *
 * @author yufeng
 * @since 2025/5/9 9:47
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(BusinessException.class)
    public BaseResponse handleBusinessException(BusinessException e){
        return ResultUtil.error(e.getCode(),e.getMessage(),e.getDescription());
    }


    @ExceptionHandler(RuntimeException.class)
    public BaseResponse handleRuntimeException(RuntimeException e){
        return ResultUtil.error(ErrorCode.SYSTEM_ERROR);
    }
}
