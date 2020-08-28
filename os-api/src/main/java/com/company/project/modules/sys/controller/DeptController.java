package com.company.project.modules.sys.controller;

import com.company.project.core.Result;
import org.springframework.web.bind.annotation.*;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.company.project.modules.sys.service.DeptService;
import com.company.project.modules.sys.entity.Dept;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 部门管理 前端控制器
 * </p>
 *
 * @author author
 * @since 2020-08-28
 */
@RestController
@RequestMapping("sys/dept")
public class DeptController {
    private final DeptService deptService;

    public DeptController(DeptService deptService) {
        this.deptService = deptService;
    }

    @PostMapping
    public Result<?> post(@RequestBody Dept dept) {
        deptService.save(dept);
        return Result.success();
    }

    @DeleteMapping
    public Result<?> delete(@RequestBody List<Long> ids) {
        deptService.removeByIds(ids);
        return Result.success();
    }

    @PutMapping
    public Result<?> put(@RequestBody Dept dept) {
        deptService.updateById(dept);
        return Result.success();
    }

    @GetMapping("/{id}")
    public Result<Dept> get(@PathVariable Integer id) {
        Dept dept = deptService.getById(id);
        return Result.success(dept);
    }

    @GetMapping
    public Result<Page<Dept>> get(@RequestParam(defaultValue = "0") Integer current, @RequestParam(defaultValue = "10") Integer size, @RequestParam Map<String, Object> params) {
        Page<Dept> page = deptService.page(new Page<>(current, size, true), new QueryWrapper<Dept>()
                .eq(Objects.nonNull(params.get("key")), "key", params.get("key"))
        );
        return Result.success(page);
    }
}