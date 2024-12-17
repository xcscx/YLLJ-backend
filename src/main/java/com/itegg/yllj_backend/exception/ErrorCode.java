package com.itegg.yllj_backend.exception;

import lombok.Getter;

/**
 * 自定义错误码
 * @author xtx
 */
@Getter
public enum ErrorCode {

    SECCESS(0, "ok"),
    PARAMS_ERROR(40000, "请求参数错误"),
    NOT_LOGIN_ERROR(40100, "未登录"),
    NO_AUTH_ERROR(40101, "权限不足"),
    FORBIDDEN_ERROR(40300, "禁止访问"),
    NOT_FOUND_ERROR(40400, "请求数据不存在"),
    REQUEST_TYPE_ERROR(40500, "请求方法错误"),
    SYSTEM_ERROR(50000,"系统内部异常"),
    OPERATION_ERROR(50001, "操作失败");

    /**
     * 状态码
     */
    private final int code;

    /**
     * 消息
     */
    private final String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

}
