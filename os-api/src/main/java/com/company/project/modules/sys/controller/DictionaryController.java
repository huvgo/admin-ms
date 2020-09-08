package com.company.project.modules.sys.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.company.project.component.annotation.Log2DB;
import com.company.project.component.annotation.Permissions;
import com.company.project.core.Results;
import com.company.project.core.Result;
import com.company.project.core.Results;
import com.company.project.modules.sys.entity.Dictionary;
import com.company.project.modules.sys.entity.Option;
import com.company.project.modules.sys.service.DictionaryService;
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
@RequestMapping("sys/dictionary")
public class DictionaryController {
    private final DictionaryService dictionaryService;

    public DictionaryController(DictionaryService dictionaryService){
        this.dictionaryService = dictionaryService;
    }

    @PostMapping
    @Log2DB
    @Permissions
    public Result<?> post(@RequestBody Dictionary dictionary){
        dictionaryService.save(dictionary);
        return Results.SUCCESS;
    }

    @DeleteMapping
    @Log2DB
    @Permissions
    public Result<?> delete(@RequestBody List<Long> ids){
        dictionaryService.removeByIds(ids);
        return Results.SUCCESS;
    }

    @PutMapping
    @Log2DB
    @Permissions
    public Result<?> put(@RequestBody Dictionary dictionary){
        dictionaryService.updateById(dictionary);
        return Results.SUCCESS;
    }

    @GetMapping("/{id}")
    @Permissions
    public Result<Dictionary> get(@PathVariable Integer id){
        Dictionary dictionary = dictionaryService.getById(id);
        return Results.success(dictionary);
    }

    @GetMapping
    @Permissions
    public Result<Page<Dictionary>> get(@RequestParam(defaultValue = "0") Integer currentPage, @RequestParam(defaultValue = "10") Integer pageSize, @RequestParam Map<String, Object> params){
        Page<Dictionary> page = dictionaryService.page(new Page<>(currentPage, pageSize, true), new QueryWrapper<Dictionary>()
                .like(!StrUtil.isBlankIfStr(params.get("name")), "name", params.get("name"))
        );
        return Results.success(page);
    }

    @GetMapping("/option")
    public Result<HashMap<String, List<Option>>> option(@RequestParam String codes){
        String[] codeArray = codes.split(",");
        HashMap<String, List<Option>> map = new HashMap<>();
        for(String code : codeArray){
            Dictionary dictionary = dictionaryService.getByCode(code);
            List<Option> options = dictionary.getOptions();
            map.put(code + "Options", options);
        }
        return Results.success(map);
    }

}