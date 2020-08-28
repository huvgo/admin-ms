package com.company.project.modules.dev.controller;

import cn.hutool.db.Entity;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.company.project.common.annotation.Permissions;
import com.company.project.core.Result;
import com.company.project.modules.dev.service.CodeService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;
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
    public Result<Page<Entity>> get(@RequestParam(defaultValue = "1") Integer currentPage, @RequestParam(defaultValue = "10") Integer pageSize, @RequestParam Map<String, Object> params) throws SQLException {
        Page<Entity> page = codeService.page(currentPage, pageSize, params);
        return Result.success(page);
    }
}
