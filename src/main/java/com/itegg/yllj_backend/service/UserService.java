package com.itegg.yllj_backend.service;

import com.itegg.yllj_backend.model.dto.user.UserLoginRequest;
import com.itegg.yllj_backend.model.dto.user.UserRegisterRequest;
import com.itegg.yllj_backend.model.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itegg.yllj_backend.model.vo.LoginUserVO;

import javax.servlet.http.HttpServletRequest;

/**
 *
 */
public interface UserService extends IService<User> {

    /**
     * 用户注册
     * @param userRegisterRequest 用户注册参数
     * @return 用户id
     */
    long userRegister(UserRegisterRequest userRegisterRequest);

    /**
     * 用户登录
     * @param userLoginRequest 用户登录参数
     * @return 登录用户的脱敏数据
     */
    LoginUserVO userLogin(UserLoginRequest userLoginRequest, HttpServletRequest request);

    /**
     * 用户注销 - 退出登录
     * @param request http请求
     * @return 是否成功登出
     */
    boolean userLogout(HttpServletRequest request);

    /**
     * 密码加密
     * @param userPassword 用户密码
     * @return
     */
    String getEncryptPassword(String userPassword);

    /**
     * 获取当前登录用户
     * @param request http请求
     * @return 用户信息
     */
    User getLoginUser(HttpServletRequest request);

    /**
     * 获取脱敏用户信息
     * @param user 用户信息
     * @return 脱敏处理后信息
     */
    LoginUserVO getLoginUserVO(User user);
}
