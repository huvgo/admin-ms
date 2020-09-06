package com.company.project.core;

/**
 * 响应码枚举，参考HTTP状态码的语义
 */
public enum ErrorCode {
    WARNING("A0000"),//用户警告
    ERROR("B0000"),//未知错误

    NOT_LOGIN("A0001"),//未登录
    UNAUTHORIZED("A0002"),//未认证
    LOGIN_EXPIRED("A0003"),//登录过期
    NOT_FOUND("A0004"),//接口不存在

    FILE_UPLOAD_FAIL("B0004");//文件上传失败

    private final String errorCode;


    ErrorCode(String errorCode) {
        this.errorCode = errorCode;

    }

    public String value() {
        return errorCode;
    }
}
