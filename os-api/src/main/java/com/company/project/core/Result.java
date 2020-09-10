package com.company.project.core;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

/**
 * 统一API响应结果封装
 */
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class Result<T> {

    private final boolean success;
    private final String userTips;
    private final String errorCode;
    private final String errorMessage;
    private final T data;

    protected Result(boolean success, String userTips, String errorCode, String errorMessage, T data) {
        this.success = success;
        this.userTips = userTips;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.data = data;
    }

    public Result<T> setUserTips(String userTips) {
        return new Result<>(this.success, userTips, this.errorCode, errorMessage, this.data);

    }

    public Result<T> setErrorCode(String errorCode) {
        return new Result<>(this.success, this.userTips, errorCode, errorMessage, this.data);
    }

    public Result<T> setErrorMessage(String errorMessage) {
        return new Result<>(this.success, this.userTips, this.errorCode, errorMessage, this.data);
    }

    public <E> Result<E> setData(E data) {
        return new Result<>(this.success, this.userTips, this.errorCode, this.errorMessage, data);
    }
}
