package com.company.project.modules.sys.controller;

import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.company.project.common.annotation.Permissions;
import com.company.project.core.Assert;
import com.company.project.core.Result;
import com.company.project.modules.sys.entity.User;
import com.company.project.modules.sys.service.CacheService;
import com.company.project.modules.sys.service.MenuService;
import com.company.project.modules.sys.service.RoleService;
import com.company.project.modules.sys.service.UserService;
import org.springframework.web.bind.annotation.*;

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
public class UserController {
    private final UserService userService;
    private final MenuService menuService;
    private final RoleService roleService;
    private final CacheService cacheService;

    public UserController(UserService userService, MenuService menuService, RoleService roleService, CacheService cacheService) {
        this.userService = userService;
        this.menuService = menuService;
        this.roleService = roleService;
        this.cacheService = cacheService;
    }

    @Permissions
    @PostMapping
    public Result<Object> post(@RequestBody User user) {
        userService.save(user);
        return Result.success();
    }

    @Permissions
    @DeleteMapping
    public Result<Object> delete(@RequestBody List<Long> ids) {
        userService.removeByIds(ids);
        return Result.success();
    }

    @Permissions
    @PutMapping
    public Result<Object> put(@RequestBody User user) {
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

        QueryWrapper<User> queryWrapper = new QueryWrapper<User>().eq(deptIdCondition, "dept_Id", params.get("deptId")).or()
                .apply(deptIdCondition, "FIND_IN_SET({0},dept_Ids)", params.get("deptId"));
        Page<User> page = userService.page(new Page<>(currentPage, pageSize, true), queryWrapper);
        return Result.success(page);
    }

    @PostMapping("/login")
    public Result<Object> login(@RequestBody User user) {
        User currentUser = userService.login(user.getUsername(), user.getPassword());
        // 重新刷新缓存
        String token = cacheService.getToken(currentUser.getUsername());
        if (token == null) {
            token = IdUtil.fastSimpleUUID();
            cacheService.putToken(currentUser.getUsername(), token);
        }
        cacheService.putUser(token, currentUser);
        return Result.success(Dict.create().set("token", token));
    }

    @GetMapping("/token")
    public Result<Object> token(String token) {
        User user = cacheService.getUser(token);
        Assert.requireNonNull(user, "登录过期,请重新登陆");
        return Result.success(user);
    }

    @PostMapping("/logout")
    public Result<Object> logout() {
        return Result.success();
    }
}
