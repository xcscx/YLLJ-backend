package com.itegg.yllj_backend.common;

import com.itegg.yllj_backend.exception.ErrorCode;

public class ResultUtils {

    /**
     * 成功请求, 返回数据体
     * @param data 返回参数
     */
    public static <T> Result<T> ok(T data) {
        return new Result<>(0, data, "ok");
    }

    /**
     * 错误请求，返回错误码信息
     * @param errorCode 错误码
     */
    public static Result<?> error(ErrorCode errorCode) {
        return new Result<>(errorCode);
    }

    /**
     * 错误请求，返回错误码信息
     * @param errorCode 错误码
     */
    public static Result<?> error(ErrorCode errorCode, String message) {
        return new Result<>(errorCode.getCode(), null, message);
    }

    /**
     * 错误请求，返回错误码信息
     * @param errorCode 错误码
     */
    public static Result<?> error(int errorCode, String message) {
        return new Result<>(errorCode, null, message);
    }

}
