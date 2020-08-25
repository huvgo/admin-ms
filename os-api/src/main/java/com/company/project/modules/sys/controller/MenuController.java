package com.company.project.modules.sys.controller;

import cn.hutool.core.lang.Dict;
import com.company.project.core.Result;
import org.springframework.web.bind.annotation.*;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.company.project.modules.sys.service.MenuService;
import com.company.project.modules.sys.entity.Menu;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.web.bind.annotation.RestController;

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


    @GetMapping("/test")
    public String test() {
        String s = "[\n" +
                "      {\n" +
                "        path: 'index',\n" +
                "        component: 'table',\n" +
                "        name: 'Profile',\n" +
                "      }\n" +
                "    ]";
        return s;
    }
}