package com.company.project.core;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * 统一API响应结果封装
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class Result<T> {

    private boolean success;
    private String userTips;
    private String errorCode;
    private String errorMessage;
    private T data;

    public Result(){
    }

    public Result(boolean success, String userTips, T data){
        this.success = success;
        this.userTips = userTips;
        this.data = data;
    }

    public Result(boolean success, String userTips, String errorCode, String errorMessage){
        this.success = success;
        this.userTips = userTips;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public Result(boolean success, String userTips, String errorCode, String errorMessage, T data){
        this.success = success;
        this.userTips = userTips;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.data = data;
    }

    public Result<T> setUserTips(String userTips){
        return new Result<>(this.success, userTips, this.errorCode, errorMessage, this.data);

    }

    public Result<T> setErrorCode(String errorCode){
        return new Result<>(this.success, this.userTips, errorCode, errorMessage, this.data);
    }

    public <E> Result<E> setData(E data){
        return new Result<>(this.success, this.userTips, this.errorCode, this.errorMessage, data);
    }

    public Result<T> setErrorMessage(String errorMessage){
        return new Result<>(this.success, this.userTips, this.errorCode, errorMessage, this.data);
    }
}
