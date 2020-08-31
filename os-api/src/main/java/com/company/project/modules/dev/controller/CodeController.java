package com.company.project.modules.dev.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.company.project.component.annotation.Permissions;
import com.company.project.core.Result;
import com.company.project.modules.dev.entity.code.Table;
import com.company.project.modules.dev.service.CodeService;
import freemarker.template.TemplateException;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 代码生成 前端控制器
 * </p>
 *
 * @author root
 */
@RestController
@RequestMapping("/dev/code")
public class CodeController {

    private final CodeService codeService;

    public CodeController(CodeService codeService) {
        this.codeService = codeService;
    }

    @GetMapping
    @Permissions
    public Result<Page<Table>> get(@RequestParam(defaultValue = "1") Integer currentPage, @RequestParam(defaultValue = "10") Integer pageSize, @RequestParam Map<String, Object> params) throws SQLException {
        Page<Table> page = codeService.page(currentPage, pageSize, params);
        return Result.success(page);
    }

    @PostMapping("/generator")
    public Result<Object> generator(@RequestBody List<Table> tableList) throws SQLException, IOException, TemplateException {
        for (Table table : tableList) {
            if (table.isGenerator()) {
                codeService.generator(table, true);
            }
        }
        return Result.success();
    }

}
