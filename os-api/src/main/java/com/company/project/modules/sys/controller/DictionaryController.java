package com.company.project.modules.sys.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.company.project.core.Result;
import com.company.project.modules.sys.entity.Dictionary;
import com.company.project.modules.sys.entity.Option;
import com.company.project.modules.sys.service.DictionaryService;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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

    @GetMapping("/option")
    public Result<HashMap<String, List<Option>>> option(@RequestParam String codes) {
        String[] codeArray = codes.split(",");
        HashMap<String, List<Option>> map = new HashMap<>();
        for (String code : codeArray) {
            Dictionary dictionary = dictionaryService.getByCode(code);
            List<Option> options = dictionary.getOptions();
            map.put(code + "Options", options);
        }
        return Result.success(map);
    }

}