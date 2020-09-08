package com.company.project.core;

/**
 * 服务（业务）异常如“ 账号或密码错误 ”，该异常只做INFO级别的日志记录
 */
public class ServiceException extends RuntimeException {

    private Result<?> error;


    public ServiceException(Result<?> result){
        this.error = result;
    }


    public Result<?> getResult(){
        return error;
    }
}
