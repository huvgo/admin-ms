package com.company.project.modules.system.controller;

import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.servlet.ServletUtil;
import cn.hutool.http.useragent.UserAgent;
import cn.hutool.http.useragent.UserAgentUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.company.project.cache.UserCacheUtil;
import com.company.project.component.annotation.RequirePermission;
import com.company.project.component.annotation.SaveLog;
import com.company.project.component.plugin.DataScopeQueryWrapper;
import com.company.project.core.Assert;
import com.company.project.core.Result;
import com.company.project.core.Results;
import com.company.project.modules.base.controller.BaseController;
import com.company.project.modules.common.service.UserCacheService;
import com.company.project.modules.system.constant.LogTypeConst;
import com.company.project.modules.system.entity.Log;
import com.company.project.modules.system.entity.User;
import com.company.project.modules.system.entity.UserNotice;
import com.company.project.modules.system.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 系统用户 前端控制器
 * </p>
 *
 * @author root
 * @since 2020-08-18
 */
@Slf4j
@RestController
@RequestMapping("/system/user")
public class UserController extends BaseController {
    private final UserService userService;
    private final UserCacheService userCacheService;
    private final NoticeService noticeService;
    private final UserNoticeService userNoticeService;
    private final LogService logService;
    private final DeptService deptService;
    private final RoleService roleService;


    public UserController(UserService userService, UserCacheService userCacheService, NoticeService noticeService, UserNoticeService userNoticeService, LogService logService, DeptService deptService, RoleService roleService) {
        this.userService = userService;
        this.userCacheService = userCacheService;
        this.noticeService = noticeService;
        this.userNoticeService = userNoticeService;
        this.logService = logService;
        this.deptService = deptService;
        this.roleService = roleService;
    }

    @RequirePermission
    @PostMapping
    @SaveLog
    public Result<?> post(@RequestBody @Validated User user) {
        userService.encodePassword(user);
        userService.save(user);
        return Results.SUCCESS;
    }

    @RequirePermission
    @DeleteMapping
    @SaveLog
    public Result<?> delete(@RequestBody List<Long> ids) {
        userService.removeByIds(ids);
        return Results.SUCCESS;
    }

    @RequirePermission
    @PutMapping
    @SaveLog
    public Result<?> put(@RequestBody User user) {
        if (StrUtil.isNotBlank(user.getPassword())) {
            userService.encodePassword(user);
        }
        // 冻结状态
        boolean status = user.isEnabled();
        if (!status) {
            boolean isNotOwn = !user.getId().equals(userCacheService.getCurrentUser().getId());
            Assert.requireTrue(isNotOwn, Results.NOT_FREEZE_SELF);
            String token = userCacheService.getToken(user.getUsername());
            if (token != null) {
                userCacheService.deleteUser(token);
            }
        }
        userService.updateById(user);
        return Results.SUCCESS;
    }

    @RequirePermission
    @GetMapping("/{id}")
    public Result<User> get(@PathVariable Integer id) {
        User user = userService.getById(id);
        return Results.SUCCESS.setData(user);
    }

    @RequirePermission
    @GetMapping
    public Result<Page<User>> get(@RequestParam(defaultValue = "0") Integer currentPage, @RequestParam(defaultValue = "10") Integer pageSize, @RequestParam Map<String, Object> params) {

        boolean deptIdCondition = StrUtil.isNotEmpty((String) params.get("deptId"));
        boolean usernameCondition = StrUtil.isNotEmpty((String) params.get("username"));
        boolean mobileCondition = StrUtil.isNotEmpty((String) params.get("mobile"));

        QueryWrapper<User> dataScopeQueryWrapper = new DataScopeQueryWrapper<User>(deptService)
                .like(usernameCondition, "username", params.get("username"))
                .eq(mobileCondition, "mobile", params.get("mobile"))
                .and(deptIdCondition, i -> i.eq("dept_Id", params.get("deptId")).or().apply("JSON_CONTAINS(dept_Ids,{0})", params.get("deptId")));
        Page<User> page = userService.page(new Page<>(currentPage, pageSize, true), dataScopeQueryWrapper);
        return Results.SUCCESS.setData(page);
    }

    @PostMapping("/login")
    public Result<Object> login(@RequestBody User user, HttpServletRequest httpRequest) {
        User currentUser = userService.login(user.getUsername(), user.getPassword());
        boolean status = currentUser.isEnabled();
        Assert.requireTrue(status, Results.ACCOUNT_EXCEPTION);
        // 重新刷新缓存
        String token = userCacheService.getToken(currentUser.getUsername());
        if (token == null) {
            token = IdUtil.fastSimpleUUID();
            userCacheService.putToken(currentUser.getUsername(), token);
        }
        userCacheService.putUser(token, currentUser);

        try {
            Log log = new Log();
            String userAgent = ServletUtil.getHeader(httpRequest, "User-Agent", "UTF-8");
            UserAgent ua = UserAgentUtil.parse(userAgent);
            HashMap<String, Object> content = new HashMap<>();
            content.put("os", ua.getPlatform().toString());
            content.put("browser", ua.getBrowser().toString());
            log.setType(LogTypeConst.LOGIN_LOG).setContent(content).setIp(ServletUtil.getClientIP(httpRequest)).setOperator(user.getUsername()).setCreateTime(new Date());
            logService.save(log);
        } catch (Exception e) {
            log.warn("日志记录失败：{}", e.getMessage());
        }

        return Results.SUCCESS.setData(Dict.create().set("token", token));
    }

    @GetMapping("/token")
    public Result<Object> token(String token) {
        User user = userCacheService.getUser(token);
        Assert.requireNonNull(user, Results.NOT_LOGGED_IN);
        return Results.SUCCESS.setData(user);
    }

    @PostMapping("/logout")
    public Result<?> logout() {
        return Results.SUCCESS;
    }


    @PostMapping("/upload")
    public Result<Object> upload(@RequestParam("file") MultipartFile file) {
        String moduleDir = "avatar";
        String path = userService.upload(file, moduleDir);
        User user = new User();
        user.setId(UserCacheUtil.getCurrentUser().getId());
        user.setAvatar(path);
        userService.updateById(user);
        return Results.SUCCESS.setData(path);
    }


    @DeleteMapping("/notice")
    public Result<?> clearNotice() {
        User currentUser = UserCacheUtil.getCurrentUser();
        UserNotice userNotice = userNoticeService.getByUserId(currentUser.getId());
        userNotice.getNoticeIds().clear();
        userNoticeService.updateById(userNotice);
        return Results.SUCCESS;
    }

}
