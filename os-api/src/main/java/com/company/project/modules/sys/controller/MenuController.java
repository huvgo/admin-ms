package com.company.project.modules.sys.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNode;
import cn.hutool.core.lang.tree.TreeUtil;
import com.company.project.core.Result;
import com.company.project.modules.sys.entity.Menu;
import com.company.project.modules.sys.service.MenuService;
import org.springframework.web.bind.annotation.*;

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

    public MenuController(MenuService menuService) {
        this.menuService = menuService;
    }

    @PostMapping
    public Result<?> add(@RequestBody Menu menu) {
        menuService.save(menu);
        return Result.success();
    }

    @DeleteMapping
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
    public Result<List<Menu>> list() {
        List<Menu> list = menuService.list();
        for (Menu menu1 : list) {
            if (menu1.getParentId() != 0) {
                for (Menu menu2 : list) {
                    if (menu1.getParentId().equals(menu2.getId())) {
                        menu1.setRemark(Dict.create().set("parentName", menu2.getName()));
                    }
                }
            } else {
                menu1.setRemark(Dict.create().set("parentName", ""));
            }

        }
        return Result.success(list);
    }


    @GetMapping("/tree")
    public Result<List<Tree<String>>> tree() {
        List<Menu> menuList = menuService.list();
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
