package com.company.project.modules.process.controller;

import cn.hutool.core.util.StrUtil;
import com.company.project.core.Results;
import com.company.project.core.Result;
import org.springframework.web.bind.annotation.*;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.company.project.modules.process.service.TaskInstanceService;
import com.company.project.modules.process.entity.TaskInstance;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.web.bind.annotation.RestController;

/**
* <p>
    *  前端控制器
    * </p>
*
* @author codeGenerator
* @since 2020-09-19
*/
@RestController
@RequestMapping("/process/taskInstance")
public class TaskInstanceController {
private final TaskInstanceService taskInstanceService;

public TaskInstanceController(TaskInstanceService taskInstanceService) {
this.taskInstanceService = taskInstanceService;
}

@PostMapping
public Result<?> post(@RequestBody TaskInstance taskInstance) {
taskInstanceService.save(taskInstance);
return Results.SUCCESS;
}

@DeleteMapping
public Result<?> delete(@RequestBody List
<Long> ids) {
    taskInstanceService.removeByIds(ids);
    return Results.SUCCESS;
    }

    @PutMapping
    public Result<?> put(@RequestBody TaskInstance taskInstance) {
    taskInstanceService.updateById(taskInstance);
    return Results.SUCCESS;
    }

    @GetMapping("/{id}")
    public Result<TaskInstance> get(@PathVariable Integer id) {
    TaskInstance taskInstance = taskInstanceService.getById(id);
    return Results.SUCCESS.setData(taskInstance);
    }

    @GetMapping
    public Result <Page <TaskInstance>> get(@RequestParam(defaultValue = "0") Integer currentPage, @RequestParam(defaultValue = "10") Integer pageSize, @RequestParam Map<String, Object> params) {
        QueryWrapper<TaskInstance> queryWrapper = new QueryWrapper<>();
        Page<TaskInstance> page = taskInstanceService.page(new Page<>(currentPage, pageSize, true), queryWrapper);
        return Results.SUCCESS.setData(page);
        }
        }