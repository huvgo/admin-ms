package com.company.project.modules.system.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.servlet.ServletUtil;
import cn.hutool.http.useragent.UserAgent;
import cn.hutool.http.useragent.UserAgentUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.company.project.cache.UserCache;
import com.company.project.cache.UserCacheUtil;
import com.company.project.component.annotation.Log2DB;
import com.company.project.component.annotation.Permissions;
import com.company.project.component.mybatis.DataScopeQueryWrapper;
import com.company.project.core.Assert;
import com.company.project.core.Result;
import com.company.project.core.Results;
import com.company.project.modules.base.controller.BaseController;
import com.company.project.modules.base.entity.BaseEntity;
import com.company.project.modules.system.constant.LogType;
import com.company.project.modules.system.entity.Log;
import com.company.project.modules.system.entity.Notice;
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
import java.util.stream.Collectors;

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
    private final UserCache userCache;
    private final NoticeService noticeService;
    private final UserNoticeService userNoticeService;
    private final LogService logService;
    private final DeptService deptService;
    private final RoleService roleService;


    public UserController(UserService userService, UserCache userCache, NoticeService noticeService, UserNoticeService userNoticeService, LogService logService, DeptService deptService, RoleService roleService) {
        this.userService = userService;
        this.userCache = userCache;
        this.noticeService = noticeService;
        this.userNoticeService = userNoticeService;
        this.logService = logService;
        this.deptService = deptService;
        this.roleService = roleService;
    }

    @Permissions
    @PostMapping
    @Log2DB
    public Result<?> post(@RequestBody @Validated User user) {
        userService.encodePassword(user);
        userService.save(user);
        return Results.SUCCESS;
    }

    @Permissions
    @DeleteMapping
    @Log2DB
    public Result<?> delete(@RequestBody List<Long> ids) {
        userService.removeByIds(ids);
        return Results.SUCCESS;
    }

    @Permissions
    @PutMapping
    @Log2DB
    public Result<?> put(@RequestBody User user) {
        if (StrUtil.isNotBlank(user.getPassword())) {
            userService.encodePassword(user);
        }
        // 冻结状态
        boolean status = user.getEnabled();
        if (!status) {
            boolean isNotOwn = !user.getId().equals(UserCacheUtil.getCurrentUser().getId());
            Assert.requireTrue(isNotOwn, Results.NOT_FREEZE_SELF);
            String token = userCache.getToken(user.getUsername());
            if (token != null) {
                userCache.deleteUser(token);
            }
        }
        userService.updateById(user);
        return Results.SUCCESS;
    }

    @Permissions
    @GetMapping("/{id}")
    public Result<User> get(@PathVariable Integer id) {
        User user = userService.getById(id);
        return Results.success(user);
    }

    @Permissions
    @GetMapping
    public Result<Page<User>> get(@RequestParam(defaultValue = "0") Integer currentPage, @RequestParam(defaultValue = "10") Integer pageSize, @RequestParam Map<String, Object> params) {

        boolean deptIdCondition = StrUtil.isNotEmpty((String) params.get("deptId"));
        boolean usernameCondition = StrUtil.isNotEmpty((String) params.get("username"));
        boolean mobileCondition = StrUtil.isNotEmpty((String) params.get("mobile"));

        QueryWrapper<User> dataScopeQueryWrapper = new DataScopeQueryWrapper<User>(deptService, roleService)
                .like(usernameCondition, "username", params.get("username"))
                .eq(mobileCondition, "mobile", params.get("mobile"))
                .and(deptIdCondition, i -> i.eq("dept_Id", params.get("deptId")).or().apply("JSON_CONTAINS(dept_Ids,{0})", params.get("deptId")));
        Page<User> page = userService.page(new Page<>(currentPage, pageSize, true), dataScopeQueryWrapper);
        return Results.success(page);
    }

    /*
    密码:111111
    盐:860effd2852141adad4dd5b256209b4a
    加密后密码:44944f63ddca4e2d1c77329df9e0d751
     */
    @PostMapping("/login")
    public Result<Object> login(@RequestBody User user, HttpServletRequest httpRequest) {
        User currentUser = userService.login(user.getUsername(), user.getPassword());
        boolean status = currentUser.getEnabled();
        Assert.requireTrue(status, Results.ACCOUNT_EXCEPTION);
        // 重新刷新缓存
        String token = userCache.getToken(currentUser.getUsername());
        if (token == null) {
            token = IdUtil.fastSimpleUUID();
            userCache.putToken(currentUser.getUsername(), token);
        }
        userCache.putUser(token, currentUser);

        try {
            Log log = new Log();
            String userAgent = ServletUtil.getHeader(httpRequest, "User-Agent", "UTF-8");
            UserAgent ua = UserAgentUtil.parse(userAgent);
            HashMap<String, Object> content = new HashMap<>();
            content.put("os", ua.getOs().toString());
            content.put("browser", ua.getBrowser().toString());
            log.setType(LogType.LOGIN_LOG).setContent(content).setIp(ServletUtil.getClientIP(httpRequest)).setOperator(user.getUsername()).setCreateTime(new Date());
            logService.save(log);
        } catch (Exception e) {
            log.warn("日志记录失败：{}", e.getMessage());
        }

        return Results.success(Dict.create().set("token", token));
    }

    @GetMapping("/token")
    public Result<Object> token(String token) {
        User user = userCache.getUser(token);
        Assert.requireNonNull(user, Results.NOT_LOGGED_IN);
        return Results.success(user);
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
        return Results.success(path);
    }

    /**
     * 用户通知
     */
    @GetMapping("/notice")
    public Result<List<Notice>> getNotice() {
        User currentUser = UserCacheUtil.getCurrentUser();
        Date now = new Date();
        // 用户上次获取消息的时间,如果没有则默认为创建用户的时间
        Date updateDate = currentUser.getCreateTime();

        // 为了获取用户上次从system_notice表拉取的消息
        UserNotice userNotice = userNoticeService.getByUserId(currentUser.getId());
        // 构造查询条件
        QueryWrapper<Notice> queryWrapper = new QueryWrapper<>();
        UserNotice finalUserNotice = userNotice;
        if (userNotice != null) {
            // 为了把用户上次拉取的未读消息也要查询出来
            updateDate = userNotice.getUpdateTime();
            Date finalUpdateDate = updateDate;
            queryWrapper.eq("is_enabled", true).and(c -> c.in(CollUtil.isNotEmpty(finalUserNotice.getNoticeIds()), "id", finalUserNotice.getNoticeIds()).or().gt("push_time", finalUpdateDate).le("push_time", now));
        } else {
            queryWrapper.eq("is_enabled", true).gt("push_time", updateDate).le("push_time", now);
        }
        List<Notice> list = noticeService.list(queryWrapper);
        List<Integer> noticeIds = list.stream().map(BaseEntity::getId).collect(Collectors.toList());

        // 更新用户通知信息
        if (userNotice == null) {
            userNotice = new UserNotice();
        }
        userNotice.setUserId(currentUser.getId());
        userNotice.setUpdateTime(now);
        userNotice.setNoticeIds(noticeIds);
        userNoticeService.saveOrUpdate(userNotice);
        return Results.success(list);
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
