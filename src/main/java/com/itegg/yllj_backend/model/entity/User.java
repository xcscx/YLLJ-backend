package com.itegg.yllj_backend.model.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 用户表
 * @TableName user
 */
@TableName(value ="user")
@Data
public class User implements Serializable {

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 用户账号
     */
    private String userAccount;

    /**
     * 密文密码
     */
    private String userPassword;

    /**
     * 用户昵称
     */
    private String userName;

    /**
     * 用户头像
     */
    private String userAvatar;

    /**
     * 用户简介
     */
    private String userProfile;

    /**
     * 用户角色 user/admin
     */
    private String userRole;

    /**
     * 最后编辑时间
     */
    private Date editTime;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 最后更新时间
     */
    private Date updateTime;

    /**
     * 最后编辑人
     */
    private Long editBy;

    /**
     * 创建人
     */
    private Long createBy;

    /**
     * 最后更新人
     */
    private Long updateBy;

    /**
     * 是否删除 0-否 1-是
     */
    @TableLogic
    private Integer removeFlag;

}