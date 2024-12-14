package com.itegg.yllj_backend.service;

import com.itegg.yllj_backend.model.dto.user.UserRegisterRequest;
import com.itegg.yllj_backend.model.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 *
 */
public interface UserService extends IService<User> {

    /**
     * 用户注册接口
     * @param userRegisterRequest 用户注册校验信息
     * @return 用户id
     */
    long userRegister(UserRegisterRequest userRegisterRequest);

    /**
     * 密码加密
     * @param userPassword 用户密码
     * @return
     */
    String getEncryptPassword(String userPassword);
}
