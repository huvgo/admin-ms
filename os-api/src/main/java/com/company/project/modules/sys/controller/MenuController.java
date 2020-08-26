package com.company.project.modules.sys.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNode;
import cn.hutool.core.lang.tree.TreeUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.company.project.common.annotation.RequiresPermissions;
import com.company.project.core.Result;
import com.company.project.modules.sys.component.UserCache;
import com.company.project.modules.sys.entity.Menu;
import com.company.project.modules.sys.entity.Role;
import com.company.project.modules.sys.entity.User;
import com.company.project.modules.sys.service.MenuService;
import com.company.project.modules.sys.service.RoleService;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;

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

    @PostMapping
    public Result<?> add(@RequestBody Menu menu) {
        menuService.save(menu);
        return Result.success();
    }

    @DeleteMapping
    @RequiresPermissions
    public Result<?> delete(@RequestBody List<Long> ids) {
        menuService.removeByIds(ids);
        return Result.success();
    }

    @PutMapping
    public Result<?> update(@RequestBody Menu menu) {
        menuService.updateById(menu);
        return Result.success();
    }

    @GetMapping("/{id}")
    public Result<Menu> detail(@PathVariable Integer id) {
        Menu menu = menuService.getById(id);
        return Result.success(menu);
    }

    @GetMapping
    @RequiresPermissions
    public Result<List<Menu>> list() {
        List<Menu> list = menuService.list();
        return Result.success(list);
    }


    @GetMapping("/tree")
    public Result<List<Tree<String>>> tree(@RequestHeader(value = "X-Token") String token) {
        User user = userCache.getUser(token);
        Assert.notNull(user, "用户信息不存在");
        List<Integer> roleIds = user.getRoleIds();
        List<Role> roles = roleService.listByIds(roleIds);
        HashSet<Integer> menuIds = new HashSet<>();
        for (Role role : roles) {
            menuIds.addAll(role.getMenuIds());
        }
        List<Menu> menuList = menuService.list(new QueryWrapper<Menu>().in("id", menuIds).ne("type", 2));
        // 构建node列表
        List<TreeNode<String>> nodeList = CollUtil.newArrayList();
        for (Menu menu : menuList) {
            TreeNode<String> treeNode = new TreeNode<>(menu.getId().toString(), menu.getParentId().toString(), menu.getName(), menu.getOrderNum());
            Dict extra = Dict.create().set("type", menu.getType()).set("path", menu.getPath()).set("meta", menu.getMeta());
            treeNode.setExtra(extra);
            nodeList.add(treeNode);
        }
        List<Tree<String>> tree = TreeUtil.build(nodeList, "0");
        return Result.success(tree);
    }
}
