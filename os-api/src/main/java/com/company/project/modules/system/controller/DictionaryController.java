package com.company.project.modules.system.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.company.project.component.annotation.SaveLog;
import com.company.project.component.annotation.RequirePermission;
import com.company.project.core.Result;
import com.company.project.core.Results;
import com.company.project.modules.system.entity.Dictionary;
import com.company.project.modules.system.entity.Option;
import com.company.project.modules.system.service.DictionaryService;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 数据字典 前端控制器
 * </p>
 *
 * @author root
 * @since 2020-08-28
 */
@RestController
@RequestMapping("/system/dictionary")
public class DictionaryController {
    private final DictionaryService dictionaryService;

    public DictionaryController(DictionaryService dictionaryService) {
        this.dictionaryService = dictionaryService;
    }

    @PostMapping
    @SaveLog
    @RequirePermission
    public Result<?> post(@RequestBody Dictionary dictionary) {
        dictionaryService.save(dictionary);
        return Results.SUCCESS;
    }

    @DeleteMapping
    @SaveLog
    @RequirePermission
    public Result<?> delete(@RequestBody List<Long> ids) {
        dictionaryService.removeByIds(ids);
        return Results.SUCCESS;
    }

    @PutMapping
    @SaveLog
    @RequirePermission
    public Result<?> put(@RequestBody Dictionary dictionary) {
        dictionaryService.updateById(dictionary);
        return Results.SUCCESS;
    }

    @GetMapping("/{id}")
    @RequirePermission
    public Result<Dictionary> get(@PathVariable Integer id) {
        Dictionary dictionary = dictionaryService.getById(id);
        return Results.SUCCESS.setData(dictionary);
    }

    @GetMapping
    @RequirePermission
    public Result<Page<Dictionary>> get(@RequestParam(defaultValue = "0") Integer currentPage, @RequestParam(defaultValue = "10") Integer pageSize, @RequestParam Map<String, Object> params) {
        Page<Dictionary> page = dictionaryService.page(new Page<>(currentPage, pageSize, true), new QueryWrapper<Dictionary>()
                .like(!StrUtil.isBlankIfStr(params.get("name")), "name", params.get("name"))
        );
        return Results.SUCCESS.setData(page);
    }

    @GetMapping("/option")
    public Result<HashMap<String, List<Option>>> option(@RequestParam String codes) {
        String[] codeArray = codes.split(",");
        HashMap<String, List<Option>> map = new HashMap<>();
        for (String code : codeArray) {
            Dictionary dictionary = dictionaryService.getByCode(code);
            List<Option> options = dictionary.getOptions();
            map.put(code + "Options", options);
        }
        return Results.SUCCESS.setData(map);
    }

}