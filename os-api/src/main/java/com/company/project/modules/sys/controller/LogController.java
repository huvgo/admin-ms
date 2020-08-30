package com.company.project.modules.sys.controller;

import com.company.project.core.Result;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.company.project.modules.sys.service.LogService;
import com.company.project.modules.sys.entity.Log;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 系统日志 前端控制器
 * </p>
 *
 * @author author
 * @since 2020-08-30
 */
@RestController
@RequestMapping("sys/log")
public class LogController {
    private final LogService logService;

    public LogController(LogService logService) {
        this.logService = logService;
    }

    @PostMapping
    public Result<?> post(@RequestBody Log log) {
        logService.save(log);
        return Result.success();
    }

    @DeleteMapping
    public Result<?> delete(@RequestBody List<Long> ids) {
        logService.removeByIds(ids);
        return Result.success();
    }

    @PutMapping
    public Result<?> put(@RequestBody Log log) {
        logService.updateById(log);
        return Result.success();
    }

    @GetMapping("/{id}")
    public Result<Log> get(@PathVariable Integer id) {
        Log log = logService.getById(id);
        return Result.success(log);
    }

    @GetMapping
    public Result<Page<Log>> get(@RequestParam(defaultValue = "0") Integer current, @RequestParam(defaultValue = "10") Integer size, @RequestParam Map<String, Object> params) {
        Page<Log> page = logService.page(new Page<>(current, size, true), new QueryWrapper<Log>()
                .eq(!StringUtils.isEmpty(params.get("operation")), "operation", params.get("operation"))
                .eq(!StringUtils.isEmpty(params.get("method")), "method", params.get("method"))
                .eq(!StringUtils.isEmpty(params.get("params")), "params", params.get("params"))
        );
        return Result.success(page);
    }
}