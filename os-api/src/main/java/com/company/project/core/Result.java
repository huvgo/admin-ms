package com.company.project.core;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 统一API响应结果封装
 */
@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Result<T> {

    private boolean success;
    private String userTips;
    private String errorCode;
    private String errorMessage;
    private T data;

    public Result(){
    }

    public Result(boolean success, String userTips, String errorCode, String errorMessage){
        this.success = success;
        this.userTips = userTips;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public Result(boolean success, String errorCode, String errorMessage, String userTips, T data){
        this.success = success;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.userTips = userTips;
        this.data = data;
    }

}
