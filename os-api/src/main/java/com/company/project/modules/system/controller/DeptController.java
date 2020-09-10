package com.company.project.modules.system.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.company.project.component.annotation.Permissions;
import com.company.project.core.Result;
import com.company.project.core.Results;
import com.company.project.modules.system.entity.Dept;
import com.company.project.modules.system.service.DeptService;
import com.company.project.modules.system.util.MenuUtil;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 部门管理 前端控制器
 * </p>
 *
 * @author author
 * @since 2020-08-29
 */
@RestController
@RequestMapping("/system/dept")
public class DeptController {
    private final DeptService deptService;

    public DeptController(DeptService deptService) {
        this.deptService = deptService;
    }

    @PostMapping
    @Permissions
    public Result<?> post(@RequestBody Dept dept) {
        deptService.save(dept);
        return Results.SUCCESS;
    }

    @DeleteMapping
    @Permissions
    public Result<?> delete(@RequestBody List<Long> ids) {
        deptService.removeByIds(ids);
        return Results.SUCCESS;
    }

    @PutMapping
    @Permissions
    public Result<?> put(@RequestBody Dept dept) {
        deptService.updateById(dept);
        return Results.SUCCESS;
    }

    @GetMapping("/{id}")
    @Permissions
    public Result<Dept> get(@PathVariable Integer id) {
        Dept dept = deptService.getById(id);
        return Results.SUCCESS.setData(dept);
    }

    @GetMapping
    @Permissions
    public Result<Page<Dept>> get(@RequestParam(defaultValue = "0") Integer currentPage, @RequestParam(defaultValue = "10") Integer pageSize, @RequestParam Map<String, Object> params) {
        boolean deptIdCondition = StrUtil.isNotEmpty((String) params.get("deptId"));
        boolean nameCondition = StrUtil.isNotEmpty((String) params.get("name"));

        QueryWrapper<Dept> queryWrapper = new QueryWrapper<Dept>()
                .like(nameCondition, "name", params.get("name"))
                .and(deptIdCondition, i -> i.eq("id", params.get("deptId")).or().apply("JSON_CONTAINS(parent_ids,{0})", params.get("deptId")));

        Page<Dept> page = deptService.page(new Page<>(currentPage, pageSize, true), queryWrapper);
        return Results.SUCCESS.setData(page);
    }

    @GetMapping("/tree")
    public Result<List<Dept>> get() {
        List<Dept> list = deptService.list();
        List<Dept> tree = MenuUtil.buildTree(list, 0);
        return Results.SUCCESS.setData(tree);
    }


    @GetMapping("/map")
    public Result<Map<Integer, String>> option() {
        Map<Integer, String> map = new HashMap<>();
        List<Dept> list = deptService.list(new QueryWrapper<Dept>().select("id", "name"));
        for (Dept dept : list) {
            map.put(dept.getId(), dept.getName());
        }
        return Results.SUCCESS.setData(map);
    }
}