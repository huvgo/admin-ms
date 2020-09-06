package com.company.project.core;

/**
 * 服务（业务）异常如“ 账号或密码错误 ”，该异常只做INFO级别的日志记录 @see WebMvcConfigurer
 */
public class ServiceException extends RuntimeException {

    private Result<Object> error = Result.error(ErrorCode.ERROR, "您好网络出现波动，请稍后再试吧", "这是一条默认错误消息，后台出现未知异常！！！");


    public ServiceException(Result<? super Object> result) {
        this.error = result;
    }


    public Result<Object> getResult() {
        return error;
    }
}
