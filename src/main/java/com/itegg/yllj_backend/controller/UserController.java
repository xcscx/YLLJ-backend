package com.itegg.yllj_backend.controller;

import cn.hutool.core.util.ObjectUtil;
import com.itegg.yllj_backend.common.Result;
import com.itegg.yllj_backend.common.ResultUtils;
import com.itegg.yllj_backend.exception.ErrorCode;
import com.itegg.yllj_backend.exception.ThrowUtils;
import com.itegg.yllj_backend.model.dto.user.UserLoginRequest;
import com.itegg.yllj_backend.model.dto.user.UserRegisterRequest;
import com.itegg.yllj_backend.model.entity.User;
import com.itegg.yllj_backend.model.vo.LoginUserVO;
import com.itegg.yllj_backend.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 用户 控制器
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    /**
     * 用户注册接口
     * @param userRegisterRequest 注册请求参数
     * @return 注册用户的id
     */
    @PostMapping("/register")
    public Result<Long> userRegister(@RequestBody UserRegisterRequest userRegisterRequest) {
        ThrowUtils.throwIf(userRegisterRequest == null, ErrorCode.PARAMS_ERROR);
        return ResultUtils.ok(userService.userRegister(userRegisterRequest));
    }

    /**
     * 用户登录接口
     * @param userLoginRequest 登录请求参数
     * @param request http请求
     * @return 用户脱敏数据
     */
    @PostMapping("/login")
    public Result<LoginUserVO> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        ThrowUtils.throwIf(ObjectUtil.isNull(userLoginRequest), ErrorCode.PARAMS_ERROR);
        return ResultUtils.ok(userService.userLogin(userLoginRequest, request));
    }

    /**
     * 获取登录用户
     * @param request http请求参数
     * @return 用户信息
     */
    @GetMapping("/get/login")
    public Result<LoginUserVO> getLoginUser(HttpServletRequest request) {
        User user = userService.getLoginUser(request);
        return ResultUtils.ok(userService.getLoginUserVO(user));
    }

}
