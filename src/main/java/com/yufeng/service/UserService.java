package com.yufeng.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.yufeng.domain.po.User;
import com.yufeng.domain.request.LoginUser;
import com.yufeng.domain.request.RegisterUser;
import com.yufeng.domain.vo.VoUser;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

/**
* @author yufeng
* @description 针对表【user(用户)】的数据库操作Service
* @createDate 2025-05-06 10:39:23
*/

public interface UserService extends IService<User> {

    public Long register(RegisterUser registerUser);
    public VoUser login(LoginUser loginUser, HttpServletRequest request);
    public List<VoUser> queryUserByUsername(String username, HttpServletRequest request);
    public Boolean deleteById(Long id,HttpServletRequest request);
    public int logout(HttpServletRequest request);


}
