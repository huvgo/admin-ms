package com.company.project.modules.system.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.company.project.component.annotation.SaveLog;
import com.company.project.component.annotation.RequirePermission;
import com.company.project.core.Result;
import com.company.project.core.Results;
import com.company.project.modules.system.entity.Role;
import com.company.project.modules.system.service.RoleService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 角色 前端控制器
 * </p>
 *
 * @author root
 * @since 2020-08-24
 */
@RestController
@RequestMapping("/system/role")
public class RoleController {
    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @RequirePermission
    @PostMapping
    @SaveLog
    public Result<?> post(@RequestBody Role role) {
        roleService.save(role);
        return Results.SUCCESS;
    }

    @RequirePermission
    @DeleteMapping
    @SaveLog
    public Result<?> delete(@RequestBody List<Long> ids) {
        roleService.removeByIds(ids);
        return Results.SUCCESS;
    }

    @RequirePermission
    @PutMapping
    @SaveLog
    public Result<?> put(@RequestBody Role role) {
        roleService.updateById(role);
        return Results.SUCCESS;
    }

    @RequirePermission
    @GetMapping("/{id}")
    public Result<Role> get(@PathVariable Integer id) {
        Role role = roleService.getById(id);
        return Results.SUCCESS.setData(role);
    }

    @RequirePermission
    @GetMapping
    public Result<Page<Role>> get(@RequestParam(defaultValue = "0") Integer currentPage, @RequestParam(defaultValue = "10") Integer pageSize, @RequestParam Map<String, Object> params) {
        QueryWrapper<Role> queryWrapper = new QueryWrapper<Role>()
                .like(!StrUtil.isBlankIfStr(params.get("name")), "name", params.get("name"));
        Page<Role> page = roleService.page(new Page<>(currentPage, pageSize, true), queryWrapper);
        return Results.SUCCESS.setData(page);
    }

    @GetMapping("/option")
    public Result<List<Role>> option() {
        List<Role> list = roleService.list(new QueryWrapper<Role>().select("id", "name"));
        return Results.SUCCESS.setData(list);
    }
}
