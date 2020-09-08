package com.company.project.modules.sys.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.company.project.component.annotation.Log2DB;
import com.company.project.component.annotation.Permissions;
import com.company.project.core.Results;
import com.company.project.core.Result;
import com.company.project.modules.sys.entity.Role;
import com.company.project.modules.sys.service.RoleService;
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
@RequestMapping("/sys/role")
public class RoleController {
    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @Permissions
    @PostMapping
    @Log2DB
    public Result<?> post(@RequestBody Role role) {
        roleService.save(role);
        return Results.SUCCESS;
    }

    @Permissions
    @DeleteMapping
    @Log2DB
    public Result<?> delete(@RequestBody List<Long> ids) {
        roleService.removeByIds(ids);
        return Results.SUCCESS;
    }

    @Permissions
    @PutMapping
    @Log2DB
    public Result<?> put(@RequestBody Role role) {
        roleService.updateById(role);
        return Results.SUCCESS;
    }

    @Permissions
    @GetMapping("/{id}")
    public Result<Role> get(@PathVariable Integer id) {
        Role role = roleService.getById(id);
        return Results.success(role);
    }

    @Permissions
    @GetMapping
    public Result<Page<Role>> get(@RequestParam(defaultValue = "0") Integer currentPage, @RequestParam(defaultValue = "10") Integer pageSize, @RequestParam Map<String, Object> params) {
        QueryWrapper<Role> queryWrapper = new QueryWrapper<Role>()
                .like(!StrUtil.isBlankIfStr(params.get("name")), "name", params.get("name"));
        Page<Role> page = roleService.page(new Page<>(currentPage, pageSize, true), queryWrapper);
        return Results.success(page);
    }

    @GetMapping("/option")
    public Result<List<Role>> option() {
        List<Role> list = roleService.list(new QueryWrapper<Role>().select("id", "name"));
        return Results.success(list);
    }
}
