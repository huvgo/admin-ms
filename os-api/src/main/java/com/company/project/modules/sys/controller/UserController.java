package com.company.project.modules.sys.controller;

import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.company.project.cache.UserCache;
import com.company.project.cache.UserCacheUtil;
import com.company.project.component.annotation.Log2DB;
import com.company.project.component.annotation.Permissions;
import com.company.project.core.Assert;
import com.company.project.core.Result;
import com.company.project.core.ResultCode;
import com.company.project.core.ServiceException;
import com.company.project.modules.base.controller.BaseController;
import com.company.project.modules.mon.service.FileService;
import com.company.project.modules.sys.entity.Notice;
import com.company.project.modules.sys.entity.User;
import com.company.project.modules.sys.service.NoticeService;
import com.company.project.modules.sys.service.UserService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
@RestController
@RequestMapping("/sys/user")
public class UserController extends BaseController {
    private final UserService userService;
    private final FileService fileService;
    private final UserCache userCache;
    private final NoticeService noticeService;


    public UserController(UserService userService, FileService fileService, UserCache userCache, NoticeService noticeService) {
        this.userService = userService;
        this.fileService = fileService;
        this.userCache = userCache;
        this.noticeService = noticeService;
    }

    @Permissions
    @PostMapping
    @Log2DB
    public Result<Object> post(@RequestBody User user) {
        userService.encodePassword(user);
        userService.save(user);
        return Result.success();
    }

    @Permissions
    @DeleteMapping
    @Log2DB
    public Result<Object> delete(@RequestBody List<Long> ids) {
        userService.removeByIds(ids);
        return Result.success();
    }

    @Permissions
    @PutMapping
    @Log2DB
    public Result<Object> put(@RequestBody User user) {
        if (StrUtil.isNotBlank(user.getPassword())) {
            userService.encodePassword(user);
        }
        // 冻结状态
        boolean status = user.getStatus();
        if (!status) {
            boolean isNotOwn = !user.getId().equals(UserCacheUtil.getCurrentUser().getId());
            Assert.requireTrue(isNotOwn, ResultCode.WARNING, "您不能冻结自己的账号");
            String token = userCache.getToken(user.getUsername());
            if (token != null) {
                userCache.deleteUser(token);
            }
        }
        userService.updateById(user);
        return Result.success();
    }

    @Permissions
    @GetMapping("/{id}")
    public Result<User> get(@PathVariable Integer id) {
        User user = userService.getById(id);
        return Result.success(user);
    }

    @Permissions
    @GetMapping
    public Result<Page<User>> get(@RequestParam(defaultValue = "0") Integer currentPage, @RequestParam(defaultValue = "10") Integer pageSize, @RequestParam Map<String, Object> params) {

        boolean deptIdCondition = StrUtil.isNotEmpty((String) params.get("deptId"));
        boolean usernameCondition = StrUtil.isNotEmpty((String) params.get("username"));
        boolean mobileCondition = StrUtil.isNotEmpty((String) params.get("mobile"));

        QueryWrapper<User> queryWrapper = new QueryWrapper<User>()
                .like(usernameCondition, "username", params.get("username"))
                .eq(mobileCondition, "mobile", params.get("mobile"))
                .and(deptIdCondition, i -> i.eq("dept_Id", params.get("deptId")).or().apply("FIND_IN_SET({0},dept_Ids)", params.get("deptId")));
        Page<User> page = userService.page(new Page<>(currentPage, pageSize, true), queryWrapper);
        return Result.success(page);
    }

    /*
    密码:111111
    盐:860effd2852141adad4dd5b256209b4a
    加密后密码:44944f63ddca4e2d1c77329df9e0d751
     */
    @PostMapping("/login")
    public Result<Object> login(@RequestBody User user) {
        User currentUser = userService.login(user.getUsername(), user.getPassword());
        boolean status = currentUser.getStatus();
        if (!status) {
            throw new ServiceException(ResultCode.WARNING, "您的账号因异常情况被冻结，请联系管理员");
        }
        // 重新刷新缓存
        String token = userCache.getToken(currentUser.getUsername());
        if (token == null) {
            token = IdUtil.fastSimpleUUID();
            userCache.putToken(currentUser.getUsername(), token);
        }
        userCache.putUser(token, currentUser);
        return Result.success(Dict.create().set("token", token));
    }

    @GetMapping("/token")
    public Result<Object> token(String token) {
        User user = userCache.getUser(token);
        Assert.requireNonNull(user, "登录过期,请重新登陆");
        return Result.success(user);
    }

    @PostMapping("/logout")
    public Result<Object> logout() {
        return Result.success();
    }


    @PostMapping("/upload")
    public Result<Object> upload(@RequestParam("file") MultipartFile file) {
        String moduleDir = "avatar";
        String path = fileService.upload(file, moduleDir);
        User user = new User();
        user.setId(UserCacheUtil.getCurrentUser().getId());
        user.setAvatar(path);
        userService.updateById(user);
        return Result.success(path);
    }

    @RequestMapping("/notice")
    public Result<List<Notice>> notice() {
        List<Notice> list = noticeService.list();
        return Result.success(list);
    }
}
