package com.company.project.core;

/**
 * 服务（业务）异常如“ 账号或密码错误 ”，该异常只做INFO级别的日志记录 @see WebMvcConfigurer
 */
public class ServiceException extends RuntimeException {
    private ResultCode resultCode = ResultCode.FAIL;

    public ServiceException() {
    }

    public ServiceException(ResultCode resultCode, String message) {
        super(message);
        this.resultCode = resultCode;
    }

    public ServiceException(String message) {
        super(message);
    }

    public ResultCode getCode() {
        return resultCode;
    }
}
