package com.company.project.modules.sys.controller;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.company.project.core.Result;
import com.company.project.modules.sys.component.UserCache;
import com.company.project.modules.sys.entity.User;
import com.company.project.modules.sys.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;

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
public class UserController {
    private final UserService userService;
    private final UserCache userCache;

    public UserController(UserService userService, UserCache userCache) {
        this.userService = userService;
        this.userCache = userCache;
    }

    @PostMapping
    public Result<?> add(@RequestBody User user) {
        userService.save(user);
        return Result.success();
    }

    @DeleteMapping
    public Result<?> delete(@RequestBody List<Long> ids) {
        userService.removeByIds(ids);
        return Result.success();
    }

    @PutMapping
    public Result<?> update(@RequestBody User user) {
        userService.updateById(user);
        return Result.success();
    }

    @GetMapping("/{id}")
    public Result<User> detail(@PathVariable Integer id) {
        User user = userService.getById(id);
        return Result.success(user);
    }

    @GetMapping
    public Result<Page<User>> page(@RequestParam(defaultValue = "0") Integer currentPage, @RequestParam(defaultValue = "10") Integer pageSize, @RequestParam Map<String, Object> params) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<User>()
                .eq(Objects.nonNull(params.get("key")), "key", params.get("key"));
        Page<User> page = userService.page(new Page<>(currentPage, pageSize, true), queryWrapper);
        return Result.success(page);
    }

    @PostMapping("/login")
    public Result<?> login(@RequestBody User user) throws JsonProcessingException {
        User currentUser = userService.getByUsernameAndPassword(user.getUsername(), user.getPassword());
        Assert.notNull(currentUser, "账号或密码不正确");
        String token = userCache.getToken(currentUser.getUsername());
        if (token == null) {
            token = IdUtil.simpleUUID();
            userCache.putToken(currentUser.getUsername(), token);
        }
        userCache.putUser(token, currentUser);
        return Result.success(Dict.create().set("token", token));
    }

    @GetMapping("/token")
    public Result<?> token(String token) throws JsonProcessingException {
        User user = userCache.getUser(token);
        Assert.notNull(user, "用户信息不存在");
        return Result.success(user);
    }

    @PostMapping("/logout")
    public Result<?> logout() {
        return Result.success();
    }
}
