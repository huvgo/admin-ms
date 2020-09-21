package com.company.project.modules.process.controller;

import cn.hutool.core.lang.Dict;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.company.project.core.Result;
import com.company.project.core.Results;
import com.company.project.modules.process.entity.Apply;
import com.company.project.modules.process.service.ApplyService;
import com.company.project.modules.system.service.UserService;
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
@RequestMapping("/process/apply")
public class ApplyController {

    private final ApplyService applyService;

    private final UserService userService;

    public ApplyController(ApplyService applyService, UserService userService) {
        this.applyService = applyService;
        this.userService = userService;
    }

    @PostMapping
    public Result<?> post(@RequestBody Apply apply) {
        applyService.save(apply);
        return Results.SUCCESS;
    }

    @DeleteMapping
    public Result<?> delete(@RequestBody List
            <Long> ids) {
        applyService.removeByIds(ids);
        return Results.SUCCESS;
    }

    @PutMapping
    public Result<?> put(@RequestBody Apply apply) {
        applyService.updateById(apply);
        return Results.SUCCESS;
    }

    @GetMapping("/{id}")
    public Result<Apply> get(@PathVariable Integer id) {
        Apply apply = applyService.getById(id);
        return Results.SUCCESS.setData(apply);
    }

    @GetMapping
    public Result<Page<Apply>> get(@RequestParam(defaultValue = "0") Integer currentPage, @RequestParam(defaultValue = "10") Integer pageSize, @RequestParam Map<String, Object> params) {
        QueryWrapper<Apply> queryWrapper = new QueryWrapper<>();
        Page<Apply> page = applyService.page(new Page<>(currentPage, pageSize, true), queryWrapper);
        List<Apply> applyList = page.getRecords();
        for (Apply apply : applyList) {
            String applicantName = userService.getById(apply.getApplyUserId()).getName();
            apply.setOther(Dict.create().set("applicantName", applicantName));
        }
        return Results.SUCCESS.setData(page);
    }
}