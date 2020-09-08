package com.company.project.core;

public class Results {

    public static Result<?> SUCCESS = new Result<>(true, "操作成功", "00000", "");
    public static Result<?> Fail = new Result<>(false, "操作失败", "B0000", "");

    public static Result<?> NOT_LOGGED_IN = new Result<>(false, "您需要先进行登录操作", "A0001", "没有获取到token");
    public static Result<?> LOGIN_EXPIRED = new Result<>(false, "您的登录已过期,请重新登陆", "A0002", "没有在缓存中获取到用户");
    public static Result<?> INCORRECT_ACCOUNT_OR_PASSWORD = new Result<>(false, "您输入账号或密码不正确", "A0000", "");

    public static Result<?> NOT_FREEZE_SELF = new Result<>(false, "您不能冻结自己的账号", "A0003", "没有在缓存中获取到用户");

    public static Result<?> ACCOUNT_EXCEPTION = new Result<>(false, "您的账号因异常情况被冻结，请联系管理员", "A0000", "");
    public static Result<?> MUST_SELECT_UPPER_MENU = new Result<>(false, "菜单或按钮必须选择上级菜单", "A0000", "");

    public static Result<?> NOT_FOUND = new Result<>(false, "您访问的接口不存在", "B0002", "访问的接口不存在");

    public static Result<?> UNAUTHORIZED = new Result<>(false, "您没有此操作的权限，请联系管理员为您添加权限", "B0002", "");

    public static <T> Result<T> success(T data){
        return new Result<T>().setSuccess(true).setUserTips("操作成功").setData(data);
    }

    public static <T> Result<T> error(String errorCode, String userTips){
        return new Result<T>().setSuccess(false).setErrorCode(errorCode).setUserTips(userTips);
    }

    public static <T> Result<T> error(String errorCode, String userTips, String errorMessage){
        return new Result<T>().setSuccess(false).setErrorCode(errorCode).setUserTips(userTips).setErrorMessage(errorMessage);
    }


}
