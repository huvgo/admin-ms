package com.company.project.modules.system.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.company.project.component.annotation.RequirePermission;
import com.company.project.core.Result;
import com.company.project.core.Results;
import com.company.project.modules.system.entity.Log;
import com.company.project.modules.system.service.LogService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 系统日志 前端控制器
 * </p>
 *
 * @author author
 * @since 2020-09-03
 */
@RestController
@RequestMapping("/system/log")
public class LogController {
    private final LogService logService;

    public LogController(LogService logService) {
        this.logService = logService;
    }

    @PostMapping
    @RequirePermission
    public Result<?> post(@RequestBody Log log) {
        logService.save(log);
        return Results.SUCCESS;
    }

    @DeleteMapping
    @RequirePermission
    public Result<?> delete(@RequestBody List<Long> ids) {
        logService.removeByIds(ids);
        return Results.SUCCESS;
    }

    @PutMapping
    @RequirePermission
    public Result<?> put(@RequestBody Log log) {
        logService.updateById(log);
        return Results.SUCCESS;
    }

    @GetMapping("/{id}")
    @RequirePermission
    public Result<Log> get(@PathVariable Integer id) {
        Log log = logService.getById(id);
        return Results.SUCCESS.setData(log);
    }

    @GetMapping
    @RequirePermission
    public Result<Page<Log>> get(@RequestParam(defaultValue = "0") Integer currentPage, @RequestParam(defaultValue = "10") Integer pageSize, @RequestParam Map<String, Object> params) {
        Page<Log> page = logService.page(new Page<>(currentPage, pageSize, true), new QueryWrapper<Log>()
                .like(StrUtil.isNotBlank((String) params.get("operator")), "operator", params.get("operator"))
                .eq(StrUtil.isNotBlank((String) params.get("type")), "type", params.get("type"))
                .eq(StrUtil.isNotBlank((String) params.get("method")), "method", params.get("method"))
                .like(StrUtil.isNotBlank((String) params.get("content")), "content", params.get("content"))
                .orderByDesc("create_time")
        );
        return Results.SUCCESS.setData(page);
    }
}