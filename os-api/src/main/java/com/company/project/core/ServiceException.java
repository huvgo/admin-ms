package com.company.project.core;

/**
 * 业务异常如“ 账号或密码错误 ”
 */
public class ServiceException extends RuntimeException {

    private final Result<?> error;

    public ServiceException(Result<?> result) {
        this.error = result;
    }


    public Result<?> getResult() {
        return error;
    }
}
