package com.itegg.yllj_backend.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.itegg.yllj_backend.model.dto.user.UserLoginRequest;
import com.itegg.yllj_backend.model.dto.user.UserQueryRequest;
import com.itegg.yllj_backend.model.dto.user.UserRegisterRequest;
import com.itegg.yllj_backend.model.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itegg.yllj_backend.model.vo.LoginUserVO;
import com.itegg.yllj_backend.model.vo.UserVO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

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
     * 获取脱敏用户登录信息
     * @param user 用户信息
     * @return 脱敏处理后信息
     */
    LoginUserVO getLoginUserVO(User user);

    /**
     * 获取用户详细脱敏信息
     * @param user 用户信息
     * @return 脱敏信息
     */
    UserVO getUserVO(User user);

    /**
     * 获取脱敏用户列表接口
     * @param userList 用户列表信息
     * @return 脱敏用户列表信息
     */
    List<UserVO> getUserVOList(List<User> userList);

    /**
     * 分页查询用户信息接口
     * @param userQueryRequest 分页查询参数
     * @return 符合条件的用户列表信息
     */
    QueryWrapper<User> getQueryWrapper(UserQueryRequest userQueryRequest);

}
