package com.company.project.core;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * 统一API响应结果封装
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Result<T> {
    private static final String DEFAULT_SUCCESS_MESSAGE = "操作成功";
    private static final String DEFAULT_FAIL_MESSAGE = "操作失败";

    public static Result<?> success() {
        return new Result<>().setCode(ResultCode.SUCCESS).setMessage(DEFAULT_SUCCESS_MESSAGE);
    }

    public static <T> Result<T> success(T data) {
        return new Result<T>().setCode(ResultCode.SUCCESS).setMessage(DEFAULT_SUCCESS_MESSAGE).setData(data);
    }

    public static <T> Result<T> fail() {
        return new Result<T>().setCode(ResultCode.FAIL).setMessage(DEFAULT_FAIL_MESSAGE);
    }

    public static <T> Result<T> fail(ResultCode resultCode) {
        return new Result<T>().setCode(resultCode).setMessage(DEFAULT_FAIL_MESSAGE);
    }

    public static <T> Result<T> fail(ResultCode resultCode, String msg) {
        return new Result<T>().setCode(resultCode).setMessage(msg);
    }

    private int code;
    private String message;
    private T data;

    public Result<T> setCode(ResultCode resultCode) {
        this.code = resultCode.code();
        return this;
    }

    public Result<T> setMessage(String message) {
        this.message = message;
        return this;
    }

    public Result<T> setData(T data) {
        this.data = data;
        return this;
    }
}
