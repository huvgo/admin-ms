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

    private boolean success;
    private String errorCode;
    private String errorMessage;
    private String userTips;
    private T data;


    public static Result<Object> success() {
        return new Result<>().setSuccess(true).setUserTips(DEFAULT_SUCCESS_MESSAGE);
    }

    public static <T> Result<T> success(T data) {
        return new Result<T>().setSuccess(true).setUserTips(DEFAULT_SUCCESS_MESSAGE).setData(data);
    }

    public static <T> Result<T> warning(String userTips) {
        return new Result<T>().setSuccess(false).setUserTips(userTips);
    }

    public static <T> Result<T> warning(ErrorCode errorCode, String userTips) {
        return new Result<T>().setSuccess(false).setErrorCode(errorCode.value()).setUserTips(userTips);
    }

    public static <T> Result<T> error(ErrorCode errorCode, String userTips, String errorMessage) {
        return new Result<T>().setSuccess(false).setErrorCode(errorCode.value()).setUserTips(userTips).setErrorMessage(errorMessage);
    }


    public Result<T> setSuccess(boolean success) {
        this.success = success;
        return this;
    }

    public Result<T> setErrorCode(String errorCode) {
        this.errorCode = errorCode;
        return this;
    }

    public Result<T> setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
        return this;
    }

    public Result<T> setUserTips(String userTips) {
        this.userTips = userTips;
        return this;
    }

    public Result<T> setData(T data) {
        this.data = data;
        return this;
    }
}
