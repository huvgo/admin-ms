package com.company.project.core;

/**
 * 响应码枚举，参考HTTP状态码的语义
 */
public enum ResultCode {
    SUCCESS(20000),//成功
    FAIL(40000),//失败
    UNAUTHORIZED(401),//未认证（签名错误）
    NOT_FOUND(404),//接口不存在
    INTERNAL_SERVER_ERROR(500),//服务器内部错误

    NOT_LOGIN(50000),//未登录
    LOGIN_EXPIRED(50008);//登录过期
    private final int code;

    ResultCode(int code){
        this.code = code;
    }

    public int code(){
        return code;
    }
}
