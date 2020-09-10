package com.company.project.core;

import java.util.List;

public class Results {

    public static Result<?> SUCCESS = new Result<>(true, "操作成功", "00000", null, null);
    public static Result<?> Fail = new Result<>(false, "操作失败", "B0000", null, null);


    // A开头的errorCode为警告型
    public static Result<?> NOT_LOGGED_IN = new Result<>(false, "您需要先进行登录操作", "A0001", "没有获取到token", null);
    public static Result<?> LOGIN_EXPIRED = new Result<>(false, "您的登录已过期,请重新登陆", "A0002", "没有在缓存中获取到用户", null);
    public static Result<?> INCORRECT_ACCOUNT_OR_PASSWORD = new Result<>(false, "您输入账号或密码不正确", "A0000", null, null);
    public static Result<?> NOT_FREEZE_SELF = new Result<>(false, "您不能冻结自己的账号", "A0003", "没有在缓存中获取到用户", null);
    public static Result<?> ACCOUNT_EXCEPTION = new Result<>(false, "您的账号因异常情况被冻结，请联系管理员", "A0000", null, null);
    public static Result<?> MUST_SELECT_UPPER_MENU = new Result<>(false, "菜单或按钮必须选择上级菜单", "A0000", null, null);

    // 以B开头的errorCode为错误型
    public static Result<List<String>> PARAM_ERROR = new Result<>(false, "操作失败，请校验页面填写的信息", "B0001", null, null);
    public static Result<?> UNAUTHORIZED = new Result<>(false, "您没有此操作的权限，请联系管理员为您添加权限", "B0002", null, null);
    public static Result<?> NOT_FOUND = new Result<>(false, "您访问的接口不存在", "B0003", "访问的接口不存在", null);

}
