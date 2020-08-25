package com.company.project.modules.sys.controller;

import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.ArrayUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.company.project.core.Result;
import com.company.project.core.ServiceException;
import com.company.project.modules.sys.entity.User;
import com.company.project.modules.sys.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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

    public UserController(UserService userService) {
        this.userService = userService;
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
    public Result<?> login(@RequestBody User user) {
        if ("111111".equals(user.getPassword())) {
            return Result.success(Dict.create()
                    .set("token", "admin-token"));
        } else {
            throw new ServiceException("账号或密码不正确");
        }
    }

    @GetMapping("/token")
    public Result<?> token(String token) {
        if ("admin-token".equals(token)) {
            User user = new User();
            ArrayList<String> roles = new ArrayList<>();
            roles.add("admin");
            user.setRoles(roles);
            user.setName("Super Admin");
            user.setAvatar("https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif");
            user.setIntroduction("I am a super administrator");
            return Result.success(user);
        } else {
            throw new ServiceException("账号或密码不正确");
        }
    }

    @PostMapping("/logout")
    public Result<?> logout() {
        return Result.success();
    }
}
