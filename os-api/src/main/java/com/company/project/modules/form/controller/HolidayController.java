package com.company.project.modules.form.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.company.project.core.Result;
import com.company.project.core.Results;
import com.company.project.modules.form.entity.Holiday;
import com.company.project.modules.form.service.HolidayService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 请假申请 前端控制器
 * </p>
 *
 * @author codeGenerator
 * @since 2020-09-15
 */
@RestController
@RequestMapping("/form/holiday")
public class HolidayController {
    private final HolidayService holidayService;

    public HolidayController(HolidayService holidayService) {
        this.holidayService = holidayService;
    }

    @PostMapping
    public Result<?> post(@RequestBody Holiday holiday) {
        holidayService.save(holiday);
        // 开启流程


        return Results.SUCCESS;
    }

    @DeleteMapping
    public Result<?> delete(@RequestBody List
            <Long> ids) {
        holidayService.removeByIds(ids);
        return Results.SUCCESS;
    }

    @PutMapping
    public Result<?> put(@RequestBody Holiday holiday) {
        holidayService.updateById(holiday);
        return Results.SUCCESS;
    }

    @GetMapping("/{id}")
    public Result<Holiday> get(@PathVariable Integer id) {
        Holiday holiday = holidayService.getById(id);
        return Results.SUCCESS.setData(holiday);
    }

    @GetMapping
    public Result<Page<Holiday>> get(@RequestParam(defaultValue = "0") Integer currentPage, @RequestParam(defaultValue = "10") Integer pageSize, @RequestParam Map<String, Object> params) {
        QueryWrapper<Holiday> queryWrapper = new QueryWrapper<Holiday>()
                .eq(StrUtil.isNotBlank((String) params.get("userId")), "user_id", params.get("userId"))
                .eq(StrUtil.isNotBlank((String) params.get("remark")), "remark", params.get("remark"));
        Page<Holiday> page = holidayService.page(new Page<>(currentPage, pageSize, true), queryWrapper);
        return Results.SUCCESS.setData(page);
    }
}