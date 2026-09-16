package com.yufeng.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.yufeng.constant.Constant;
import com.yufeng.domain.po.User;
import com.yufeng.domain.request.LoginUser;
import com.yufeng.domain.request.RegisterUser;
import com.yufeng.domain.vo.VoUser;
import com.yufeng.enums.ErrorCode;
import com.yufeng.exception.BusinessException;
import com.yufeng.service.UserService;
import com.yufeng.mapper.UserMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
* @author yufeng
* @description 针对表【user(用户)】的数据库操作Service实现
* @createDate 2025-05-06 10:39:23
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{
    @Autowired
    private UserMapper usermapper;

    /**
     * 用户注册
     * @param registerUser
     * @return
     */
    @Override
    public Long register(RegisterUser registerUser) {
        String username=registerUser.getUsername();
        String password = registerUser.getPassword();
        String checkPassword=registerUser.getCheckPassword();
        //1.三个任一都不许为空
        if(!StrUtil.isAllNotBlank(username,password,checkPassword)){
            throw new BusinessException(ErrorCode.NULL_ERROR,"用户名、密码、确认密码有空");
        }
        //2.验证账号的长度大于等于8小于等于15
        if(username.length()<8||username.length()>15){
            throw new BusinessException(ErrorCode.NULL_ERROR,"用户名长度不符合要求");
        }
        //3.验证密码和确定密码的长度大于8
        if(password.length()<8){
            throw new BusinessException(ErrorCode.NULL_ERROR,"密码长度小于8位");
        }
        if(checkPassword.length()<8){
            throw new BusinessException(ErrorCode.NULL_ERROR,"确认密码长度小于8位");
        }
        //4.验证密码和确定密码是否一致
        if (!password.equals(checkPassword)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"密码和确定密码不一致");
        }

        //5. 用户名是否重复
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username",username);
        User user1 = this.getOne(queryWrapper);
        if (user1!=null){//用户名重复
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"用户名已存在");
        }
        //6.将密码加密
        String md5Password = DigestUtil.md5Hex(password);
        //创建一个新的密码加密的user对象
        User user = new User();
        user.setUsername(username);
        user.setPassword(md5Password);

        //7.将数据传入数据库
        usermapper.insert(user);
        return user.getId();
    }

    /**
     * 登录功能
     * @param loginUser
     * @return
     */
    @Override
    public VoUser login(LoginUser loginUser, HttpServletRequest request) {
        String username=loginUser.getUsername();
        String password = loginUser.getPassword();
        //1.账号都不许为空
        if(!StrUtil.isAllNotBlank(username,password)){
            throw new BusinessException(ErrorCode.NULL_ERROR,"用户名、密码有空");
        }
        //2.验证账号的长度大于等于8小于等于15
        if(username.length()<8||username.length()>15){
            throw new BusinessException(ErrorCode.NULL_ERROR,"用户名长度不符合要求");
        }
        //3.验证密码的长度大于8
        if(password.length()<8){
            throw new BusinessException(ErrorCode.NULL_ERROR,"密码长度小于8位");
        }

        //4.查看数据库是否有相关的数据
        //密码加密
        String md5Password = DigestUtil.md5Hex(password);
        //根据账号查询数据
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("username", username);
        User user = getOne(userQueryWrapper);
        //账号错误
        if (user==null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"用户不存在");
        }
        //检查密码,密码错误
        if (!user.getPassword().equals(md5Password)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"密码错误");
        }
        //保存登录状态
        request.getSession().setAttribute(Constant.USER_LOGIN_STATE,user);
        //返回
        return BeanUtil.copyProperties(user, VoUser.class);
    }

    /**
     * 根据用户名查询用户
     * @param username
     * @return
     */
    @Override
    public List<VoUser> queryUserByUsername(String username, HttpServletRequest request) {
        //鉴权
        Object attribute = request.getSession().getAttribute(Constant.USER_LOGIN_STATE);
        if (attribute==null){
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        User user=(User)attribute;
        //鉴权
        if(!isAuth(user)){
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        //模糊查询用户
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.like("username",username);
        List<User> users = list(userQueryWrapper);
        //没查到用户
        if(users==null || users.isEmpty()){
            return null;
        }
        //将List<User>转换为List<VoUser>
        ArrayList<VoUser> voUsers = new ArrayList<>();
        for(User user1:users){
            VoUser voUser = BeanUtil.copyProperties(user1, VoUser.class);
            voUsers.add(voUser);
        }
        return voUsers;
    }

    /**
     * 根据id删除用户(逻辑删除)
     * @param id
     * @return
     */
    @Override
    public Boolean deleteById(Long id,HttpServletRequest request) {
        Object attribute = request.getSession().getAttribute(Constant.USER_LOGIN_STATE);
        if (attribute==null){
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        User user=(User)attribute;
        if (!isAuth(user)){
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        return removeById(id);
    }

    @Override
    public int logout(HttpServletRequest request) {
        //删除ssesion的登录态
        request.getSession().removeAttribute(Constant.USER_LOGIN_STATE);
        return 0;
    }

    /**
     * 鉴权逻辑
     * @param user
     * @return
     */
    private boolean isAuth(User user){
        //用户无权限
        if (user.getAuth() != 1){
            return false;
        }
        return true;
    }


}




