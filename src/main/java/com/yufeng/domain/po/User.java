package com.yufeng.domain.po;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;

/**
 * 用户
 * @TableName user
 */
@TableName(value ="user")
@Data
public class User implements Serializable {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 昵称
     */
    @TableField(value = "nickname")
    private String nickname;

    /**
     * 用户名
     */
    @TableField(value = "username")
    private String username;

    /**
     * 密码
     */
    @TableField(value = "password")
    private String password;

    /**
     * 头像
     */
    @TableField(value = "avatar")
    private String avatar;

    /**
     * 性别(1女2男）
     */
    @TableField(value = "gender")
    private Integer gender;

    /**
     * 邮箱
     */
    @TableField(value = "email")
    private String email;

    /**
     * 状态(0默认1封锁)
     */
    @TableField(value = "status")
    private Integer status;

    /**
     * 权限(0默认1管理员)
     */
    @TableField(value = "auth")
    private Integer auth;

    /**
     * 逻辑删除（0默认1已删除）
     */

    @TableField(value = "delete_flag")
    @TableLogic
    private Integer deleteFlag;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}