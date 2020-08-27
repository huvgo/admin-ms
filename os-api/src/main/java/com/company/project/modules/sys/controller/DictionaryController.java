package com.company.project.modules.sys.controller;

import com.company.project.core.Result;
import org.springframework.web.bind.annotation.*;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.company.project.modules.sys.service.DictionaryService;
import com.company.project.modules.sys.entity.Dictionary;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 数据字典 前端控制器
 * </p>
 *
 * @author root
 * @since 2020-08-27
 */
@RestController
@RequestMapping("sys/dictionary")
public class DictionaryController {
    private final DictionaryService dictionaryService;

    public DictionaryController(DictionaryService dictionaryService) {
        this.dictionaryService = dictionaryService;
    }

    @PostMapping
    public Result<?> post(@RequestBody Dictionary dictionary) {
        dictionaryService.save(dictionary);
        return Result.success();
    }

    @DeleteMapping
    public Result<?> delete(@RequestBody List<Long> ids) {
        dictionaryService.removeByIds(ids);
        return Result.success();
    }

    @PutMapping
    public Result<?> put(@RequestBody Dictionary dictionary) {
        dictionaryService.updateById(dictionary);
        return Result.success();
    }

    @GetMapping("/{id}")
    public Result<Dictionary> get(@PathVariable Integer id) {
        Dictionary dictionary = dictionaryService.getById(id);
        return Result.success(dictionary);
    }

    @GetMapping
    public Result<Page<Dictionary>> get(@RequestParam(defaultValue = "0") Integer current, @RequestParam(defaultValue = "10") Integer size, @RequestParam Map<String, Object> params) {
        Page<Dictionary> page = dictionaryService.page(new Page<>(current, size, true), new QueryWrapper<Dictionary>()
                .eq(Objects.nonNull(params.get("key")), "key", params.get("key"))
        );
        return Result.success(page);
    }
}