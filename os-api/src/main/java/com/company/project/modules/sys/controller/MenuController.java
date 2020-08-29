package com.company.project.modules.sys.controller;

import com.company.project.common.annotation.Permissions;
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

}
