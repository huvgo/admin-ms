package ${package.Controller};

import com.company.project.core.Result;
import org.springframework.web.bind.annotation.*;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import ${package.Service}.${table.serviceName};
import ${package.Entity}.${entity};

import java.util.List;
import java.util.Map;
import java.util.Objects;

<#if restControllerStyle>
import org.springframework.web.bind.annotation.RestController;
<#else>
import org.springframework.stereotype.Controller;
</#if>
<#if superControllerClassPackage??>
import ${superControllerClassPackage};
</#if>

/**
 * <p>
 * ${table.comment!} 前端控制器
 * </p>
 *
 * @author ${author}
 * @since ${date}
 */
<#if restControllerStyle>
@RestController
<#else>
@Controller
</#if>
@RequestMapping("<#if package.ModuleName??>/${package.ModuleName}</#if>/<#if controllerMappingHyphenStyle??>${controllerMappingHyphen}<#else>${table.entityPath}</#if>")
<#if kotlin>
class ${table.controllerName}<#if superControllerClass??> : ${superControllerClass}()</#if>
<#else>
<#if superControllerClass??>
public class ${table.controllerName} extends ${superControllerClass} {
<#else>
public class ${table.controllerName} {
</#if>
    private final ${table.serviceName} ${table.serviceName?uncap_first};

    public ${table.controllerName}(${table.serviceName} ${table.serviceName?uncap_first}) {
        this.${table.serviceName?uncap_first} = ${table.serviceName?uncap_first};
    }

    @PostMapping
    public Result<?> add(@RequestBody ${entity} ${entity?lower_case}) {
        ${table.serviceName?uncap_first}.save(${entity?lower_case});
        return Result.success();
    }

    @DeleteMapping
    public Result<?> delete(@RequestBody List<Long> ids) {
        ${table.serviceName?uncap_first}.removeByIds(ids);
        return Result.success();
    }

    @PutMapping
    public Result<?> update(@RequestBody ${entity} ${entity?lower_case}) {
        ${table.serviceName?uncap_first}.updateById(${entity?lower_case});
        return Result.success();
    }

    @GetMapping("/{id}")
    public Result<${entity}> detail(@PathVariable Integer id) {
        ${entity} ${entity?lower_case} = ${table.serviceName?uncap_first}.getById(id);
        return Result.success(${entity?lower_case});
    }

    @GetMapping
    public Result<Page<${entity}>> page(@RequestParam(defaultValue = "0") Integer current, @RequestParam(defaultValue = "10") Integer size, @RequestParam Map<String, Object> params) {
        Page<${entity}> page = ${table.serviceName?uncap_first}.page(new Page<>(current, size, true), new QueryWrapper<${entity}>()
                .eq(Objects.nonNull(params.get("id")), "id", params.get("id"))
        );
        return Result.success(page);
    }
}
</#if>
