package com.itegg.yllj_backend.controller;

import com.itegg.yllj_backend.common.Result;
import com.itegg.yllj_backend.common.ResultUtils;
import com.itegg.yllj_backend.exception.ErrorCode;
import com.itegg.yllj_backend.exception.ThrowUtils;
import com.itegg.yllj_backend.model.dto.user.UserRegisterRequest;
import com.itegg.yllj_backend.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

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

}
