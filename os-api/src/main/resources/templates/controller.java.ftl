package ${package}.controller;

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
 * ${tableComment!} 前端控制器
 * </p>
 *
 * @author ${author}
 * @since ${date}
 */
@RestController
@RequestMapping("${moduleName}/${lowerFirstName}")
public class ${upperFirstName}Controller {
    private final ${upperFirstName}Service ${lowerFirstName}Service;

    public ${upperFirstName}Controller(${upperFirstName}Service ${lowerFirstName}Service) {
        this.${lowerFirstName}Service = ${lowerFirstName}Service;
    }

    @PostMapping
    public Result<?> post(@RequestBody ${upperFirstName} ${lowerFirstName}) {
        ${lowerFirstName}Service.save(${lowerFirstName});
        return Result.success();
    }

    @DeleteMapping
    public Result<?> delete(@RequestBody List<Long> ids) {
        ${lowerFirstName}Service.removeByIds(ids);
        return Result.success();
    }

    @PutMapping
    public Result<?> put(@RequestBody ${upperFirstName} ${lowerFirstName}) {
        ${lowerFirstName}Service.updateById(${lowerFirstName});
        return Result.success();
    }

    @GetMapping("/{id}")
    public Result<${upperFirstName}> get(@PathVariable Integer id) {
        ${upperFirstName} ${lowerFirstName} = ${lowerFirstName}Service.getById(id);
        return Result.success(${lowerFirstName});
    }

    @GetMapping
    public Result<Page<${upperFirstName}>> get(@RequestParam(defaultValue = "0") Integer current, @RequestParam(defaultValue = "10") Integer size, @RequestParam Map<String, Object> params) {
        Page<${upperFirstName}> page = ${lowerFirstName}Service.page(new Page<>(current, size, true), new QueryWrapper<${upperFirstName}>()
                .eq(Objects.nonNull(params.get("key")), "key", params.get("key"))
        );
        return Result.success(page);
    }
}