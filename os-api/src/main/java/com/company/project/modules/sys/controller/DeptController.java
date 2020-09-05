package com.company.project.modules.sys.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.company.project.component.annotation.Permissions;
import com.company.project.core.Result;
import com.company.project.modules.sys.entity.Dept;
import com.company.project.modules.sys.service.DeptService;
import com.company.project.modules.sys.util.MenuUtil;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 部门管理 前端控制器
 * </p>
 *
 * @author author
 * @since 2020-08-29
 */
@RestController
@RequestMapping("sys/dept")
public class DeptController {
    private final DeptService deptService;

    public DeptController(DeptService deptService){
        this.deptService = deptService;
    }

    @PostMapping
    @Permissions
    public Result<?> post(@RequestBody Dept dept){
        deptService.save(dept);
        return Result.success();
    }

    @DeleteMapping
    @Permissions
    public Result<?> delete(@RequestBody List<Long> ids){
        deptService.removeByIds(ids);
        return Result.success();
    }

    @PutMapping
    @Permissions
    public Result<?> put(@RequestBody Dept dept){
        deptService.updateById(dept);
        return Result.success();
    }

    @GetMapping("/{id}")
    @Permissions
    public Result<Dept> get(@PathVariable Integer id){
        Dept dept = deptService.getById(id);
        return Result.success(dept);
    }

    @GetMapping
    @Permissions
    public Result<Page<Dept>> get(@RequestParam(defaultValue = "0") Integer currentPage, @RequestParam(defaultValue = "10") Integer pageSize, @RequestParam Map<String, Object> params){
        boolean deptIdCondition = StrUtil.isNotEmpty((String) params.get("deptId"));
        boolean nameCondition = StrUtil.isNotEmpty((String) params.get("name"));

        QueryWrapper<Dept> queryWrapper = new QueryWrapper<Dept>()
                .like(nameCondition, "name", params.get("name"))
                .and(deptIdCondition, i -> i.eq("id", params.get("deptId")).or().apply("FIND_IN_SET({0},parent_ids)", params.get("deptId")));

        Page<Dept> page = deptService.page(new Page<>(currentPage, pageSize, true), queryWrapper);
        return Result.success(page);
    }

    @GetMapping("/tree")
    public Result<List<Dept>> get(){
        List<Dept> list = deptService.list();
        List<Dept> tree = MenuUtil.buildTree(list, 0);
        return Result.success(tree);
    }


    @GetMapping("/map")
    public Result<Map<Integer, String>> option(){
        Map<Integer, String> map = new HashMap<>();
        List<Dept> list = deptService.list(new QueryWrapper<Dept>().select("id", "name"));
        for(Dept dept : list){
            map.put(dept.getId(), dept.getName());
        }
        return Result.success(map);
    }
}