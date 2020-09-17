package com.company.project.modules.process.controller;

import cn.hutool.core.util.StrUtil;
import com.company.project.core.Results;
import com.company.project.core.Result;
import org.springframework.web.bind.annotation.*;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.company.project.modules.process.service.InstanceService;
import com.company.project.modules.process.entity.Instance;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author codeGenerator
 * @since 2020-09-17
 */
@RestController
@RequestMapping("/process/instance")
public class InstanceController {
    private final InstanceService instanceService;

    public InstanceController(InstanceService instanceService) {
        this.instanceService = instanceService;
    }

    @PostMapping
    public Result<?> post(@RequestBody Instance instance) {
        instanceService.save(instance);
        return Results.SUCCESS;
    }

    @DeleteMapping
    public Result<?> delete(@RequestBody List
            <Long> ids) {
        instanceService.removeByIds(ids);
        return Results.SUCCESS;
    }

    @PutMapping
    public Result<?> put(@RequestBody Instance instance) {
        instanceService.updateById(instance);
        return Results.SUCCESS;
    }

    @GetMapping("/{id}")
    public Result<Instance> get(@PathVariable Integer id) {
        Instance instance = instanceService.getById(id);
        return Results.SUCCESS.setData(instance);
    }

    @GetMapping
    public Result<Page<Instance>> get(@RequestParam(defaultValue = "0") Integer currentPage, @RequestParam(defaultValue = "10")Integer pageSize, @RequestParam Map<String, Object> params) {
        QueryWrapper<Instance> queryWrapper = new QueryWrapper<>();
        Page<Instance> page = instanceService.page(new Page<>(currentPage, pageSize, true), queryWrapper);
        return Results.SUCCESS.setData(page);
    }
}