package com.company.project.modules.sys.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.company.project.core.Result;
import com.company.project.modules.sys.entity.Log;
import com.company.project.modules.sys.service.LogService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 系统日志 前端控制器
 * </p>
 *
 * @author author
 * @since 2020-09-02
 */
@RestController
@RequestMapping("sys/log")
public class LogController {
    private final LogService logService;

    public LogController(LogService logService){
        this.logService = logService;
    }

    @PostMapping
    public Result<?> post(@RequestBody Log log){
        logService.save(log);
        return Result.success();
    }

    @DeleteMapping
    public Result<?> delete(@RequestBody List<Long> ids){
        logService.removeByIds(ids);
        return Result.success();
    }

    @PutMapping
    public Result<?> put(@RequestBody Log log){
        logService.updateById(log);
        return Result.success();
    }

    @GetMapping("/{id}")
    public Result<Log> get(@PathVariable Integer id){
        Log log = logService.getById(id);
        return Result.success(log);
    }

    @GetMapping
    public Result<Page<Log>> get(@RequestParam(value = "currentPage", defaultValue = "0") Integer current, @RequestParam(value = "pageSize", defaultValue = "10") Integer size, @RequestParam Map<String, Object> params){
        Page<Log> page = logService.page(new Page<>(current, size, true), new QueryWrapper<Log>()
                .eq(StrUtil.isNotBlank((String) params.get("operator")), "operator", params.get("operator"))
                .eq(StrUtil.isNotBlank((String) params.get("operation")), "operation", params.get("operation"))
                .eq(StrUtil.isNotBlank((String) params.get("params")), "params", params.get("params"))
                .orderByDesc("create_date")
        );
        return Result.success(page);
    }
}