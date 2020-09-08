package ${package}.controller;

import cn.hutool.core.util.StrUtil;
import com.company.project.core.Results;
import com.company.project.core.Result;
import org.springframework.web.bind.annotation.*;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import ${package}.service.${upperFirstName}Service;
import ${package}.entity.${upperFirstName};

import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.web.bind.annotation.RestController;

/**
* <p>
    * ${table.comment!} 前端控制器
    * </p>
*
* @author ${author}
* @since ${date}
*/
@RestController
@RequestMapping("/${moduleName}/${lowerFirstName}")
public class ${upperFirstName}Controller {
private final ${upperFirstName}Service ${lowerFirstName}Service;

public ${upperFirstName}Controller(${upperFirstName}Service ${lowerFirstName}Service) {
this.${lowerFirstName}Service = ${lowerFirstName}Service;
}

@PostMapping
public Result<?> post(@RequestBody ${upperFirstName} ${lowerFirstName}) {
${lowerFirstName}Service.save(${lowerFirstName});
return Results.SUCCESS;
}

@DeleteMapping
public Result<?> delete(@RequestBody List
<Long> ids) {
    ${lowerFirstName}Service.removeByIds(ids);
    return Results.SUCCESS;
    }

    @PutMapping
    public Result<?> put(@RequestBody ${upperFirstName} ${lowerFirstName}) {
    ${lowerFirstName}Service.updateById(${lowerFirstName});
    return Results.SUCCESS;
    }

    @GetMapping("/{id}")
    public Result<${upperFirstName}> get(@PathVariable Integer id) {
    ${upperFirstName} ${lowerFirstName} = ${lowerFirstName}Service.getById(id);
    return Results.success(${lowerFirstName});
    }

    @GetMapping
    public Result
    <Page
    <${upperFirstName}>> get(@RequestParam(defaultValue = "0") Integer currentPage, @RequestParam(defaultValue = "10")
    Integer pageSize, @RequestParam Map
    <String
    , Object> params) {
    QueryWrapper<${upperFirstName}> queryWrapper = new QueryWrapper<>()
    <#list fields as field>
        <#if field.condition>
            .eq(StrUtil.isNotBlank((String)params.get("${field.name}")), "${field.columnName}", params.get("${field.name}"))
        </#if>
    </#list>
    Page<${upperFirstName}> page = ${lowerFirstName}Service.page(new Page<>(current, size, true), queryWrapper);
    return Results.success(page);
    }
    }