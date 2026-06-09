package com.yufeng.domain.request;

import lombok.Data;

/**
 * 注册数据
 *
 * @author yufeng
 * @since 2025/5/6 10:55
 */
@Data
public class RegisterUser {

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 确定密码
     */
    private String checkPassword;
}
