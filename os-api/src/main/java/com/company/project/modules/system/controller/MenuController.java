package com.company.project.modules.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.company.project.component.annotation.Log2DB;
import com.company.project.component.annotation.Permissions;
import com.company.project.core.Result;
import com.company.project.core.Results;
import com.company.project.modules.system.entity.Menu;
import com.company.project.modules.system.service.MenuService;
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
@RequestMapping("/system/menu")
public class MenuController {
    private final MenuService menuService;

    public MenuController(MenuService menuService) {
        this.menuService = menuService;
    }

    @Permissions
    @PostMapping
    @Log2DB
    public Result<?> post(@RequestBody Menu menu) {
        if (menu.getParentId() == 0 && menu.getType() != 0) {
            return Results.MUST_SELECT_UPPER_MENU;
        }
        menuService.save(menu);
        return Results.SUCCESS;
    }

    @Permissions
    @DeleteMapping
    @Log2DB
    public Result<?> delete(@RequestBody List<Long> ids) {
        menuService.removeByIds(ids);
        return Results.SUCCESS;
    }

    @Permissions
    @PutMapping
    @Log2DB
    public Result<?> put(@RequestBody Menu menu) {
        menuService.updateById(menu);
        return Results.SUCCESS;
    }

    @Permissions
    @GetMapping("/{id}")
    public Result<Menu> get(@PathVariable Integer id) {
        Menu menu = menuService.getById(id);
        return Results.SUCCESS.setData(menu);
    }

    @Permissions
    @GetMapping
    public Result<List<Menu>> get(@RequestParam(value = "nonButton", required = false) boolean nonButton) {
        List<Menu> list = menuService.list(new QueryWrapper<Menu>().ne(nonButton, "type", 2));
        return Results.SUCCESS.setData(list);
    }

}
