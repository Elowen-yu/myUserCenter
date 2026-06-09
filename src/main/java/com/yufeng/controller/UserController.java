package com.yufeng.controller;

import com.yufeng.domain.po.User;
import com.yufeng.domain.request.LoginUser;
import com.yufeng.domain.request.RegisterUser;
import com.yufeng.domain.response.BaseResponse;
import com.yufeng.domain.vo.VoUser;
import com.yufeng.enums.ErrorCode;
import com.yufeng.exception.BusinessException;
import com.yufeng.service.UserService;
import com.yufeng.util.ResultUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author yufeng
 * @since 2025/5/6 11:19
 */
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    /**
     * 用户注册接口
     * @param registerUser
     * @return
     */
    @PostMapping("/register")
    public BaseResponse<Long> register(@RequestBody RegisterUser registerUser){
        Long userId = userService.register(registerUser);
        return ResultUtil.success(userId);
    }

    @PostMapping("/login")
    public BaseResponse<VoUser> login(@RequestBody LoginUser loginUser, HttpServletRequest request){
        if (request==null) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        VoUser voUser = userService.login(loginUser,request);
        return ResultUtil.success(voUser);
    }

    @GetMapping("/search")
    public BaseResponse<List<VoUser>> queryUserById(String username, HttpServletRequest request){
        if (request==null) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        List<VoUser> list = userService.queryUserByUsername(username, request);
        return ResultUtil.success(list);
    }

    @DeleteMapping("/delete")
    public BaseResponse<Boolean> deleteById(@RequestParam Long id,HttpServletRequest request){
        if (request==null) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        Boolean b = userService.deleteById(id, request);
        return ResultUtil.success(b);
    }

    @PostMapping("/logout")
    public BaseResponse<Integer> logout(HttpServletRequest request){
        if (request==null) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        int userId = userService.logout(request);
        return ResultUtil.success(userId);
    }
}
