package com.company.project.modules.sys.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.company.project.common.annotation.Permissions;
import com.company.project.core.Assert;
import com.company.project.core.Result;
import com.company.project.modules.sys.util.UserCache;
import com.company.project.modules.sys.entity.Menu;
import com.company.project.modules.sys.entity.Role;
import com.company.project.modules.sys.entity.User;
import com.company.project.modules.sys.service.MenuService;
import com.company.project.modules.sys.service.RoleService;
import com.company.project.modules.sys.util.MenuUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 菜单管理 前端控制器
 * </p>
 *
 * @author root
 * @since 2020-08-24
 */
@RestController
@RequestMapping("/sys/menu")
public class MenuController {
    private final MenuService menuService;
    private final RoleService roleService;
    private final UserCache userCache;


    public MenuController(MenuService menuService, RoleService roleService, UserCache userCache) {
        this.menuService = menuService;
        this.roleService = roleService;
        this.userCache = userCache;
    }

    @Permissions
    @PostMapping
    public Result<Object> post(@RequestBody Menu menu) {
        menuService.save(menu);
        return Result.success();
    }

    @Permissions
    @DeleteMapping
    public Result<Object> delete(@RequestBody List<Long> ids) {
        menuService.removeByIds(ids);
        return Result.success();
    }

    @Permissions
    @PutMapping
    public Result<Object> put(@RequestBody Menu menu) {
        menuService.updateById(menu);
        return Result.success();
    }

    @Permissions
    @GetMapping("/{id}")
    public Result<Menu> get(@PathVariable Integer id) {
        Menu menu = menuService.getById(id);
        return Result.success(menu);
    }

    @Permissions
    @GetMapping
    public Result<List<Menu>> get() {
        List<Menu> list = menuService.list();
        return Result.success(list);
    }

    /**
     * 侧边栏
     */
    @GetMapping("/sidebar")
    public Result<List<Menu>> sidebar(@RequestHeader(value = "X-Token") String token) throws JsonProcessingException {
        User user = userCache.getUser(token);
        Assert.requireNonNull(user, "登录过期,请重新登陆");
        List<Integer> roleIds = user.getRoleIds();
        List<Role> roles = roleService.listByIds(roleIds);
        HashSet<Integer> menuIds = new HashSet<>();
        for (Role role : roles) {
            menuIds.addAll(role.getMenuIds());
        }
        List<Menu> menuList = menuService.list(new QueryWrapper<Menu>().in("id", menuIds));
        user.setMenuList(menuList);
        userCache.putUser(token, user);

        List<Menu> collect = menuList.stream().filter(menu -> menu.getType() != 2).collect(Collectors.toList());
        // 构建node列表
        List<Menu> tree = MenuUtil.buildTree(collect, 0);
        return Result.success(tree);
    }
}
