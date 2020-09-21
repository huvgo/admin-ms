package com.company.project.modules.process.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.company.project.core.Result;
import com.company.project.core.Results;
import com.company.project.modules.process.entity.Approve;
import com.company.project.modules.process.service.ApproveService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 请假申请 前端控制器
 * </p>
 *
 * @author codeGenerator
 * @since 2020-09-21
 */
@RestController
@RequestMapping("/process/approve")
public class ApproveController {
    private final ApproveService approveService;

    public ApproveController(ApproveService approveService) {
        this.approveService = approveService;
    }

    @PostMapping
    public Result<?> post(@RequestBody Approve approve) {
        approveService.save(approve);
        return Results.SUCCESS;
    }

    @DeleteMapping
    public Result<?> delete(@RequestBody List
            <Long> ids) {
        approveService.removeByIds(ids);
        return Results.SUCCESS;
    }

    @PutMapping
    public Result<?> put(@RequestBody Approve approve) {
        approveService.updateById(approve);
        return Results.SUCCESS;
    }

    @GetMapping("/{id}")
    public Result<Approve> get(@PathVariable Integer id) {
        Approve approve = approveService.getById(id);
        return Results.SUCCESS.setData(approve);
    }

    @GetMapping
    public Result<Page<Approve>> get(@RequestParam(defaultValue = "0") Integer currentPage, @RequestParam(defaultValue = "10") Integer pageSize, @RequestParam Map<String, Object> params) {
        QueryWrapper<Approve> queryWrapper = new QueryWrapper<>();
        Page<Approve> page = approveService.page(new Page<>(currentPage, pageSize, true), queryWrapper);
        return Results.SUCCESS.setData(page);
    }
}