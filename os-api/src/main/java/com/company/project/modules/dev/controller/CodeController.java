package com.company.project.modules.dev.controller;

import cn.hutool.core.io.IoUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.company.project.component.annotation.Permissions;
import com.company.project.core.Result;
import com.company.project.modules.dev.entity.code.Table;
import com.company.project.modules.dev.service.CodeService;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
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


    @PostMapping("/generate")
    public void generate(@RequestBody List<Table> tableList, HttpServletResponse response) throws Exception {
        byte[] bytes = codeService.generate(tableList);
        response.reset();
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Expose-Headers", "Content-Disposition");
        response.setHeader("Content-Disposition", "attachment; filename=\"admin-os.zip\"");
        response.addIntHeader("Content-Length", bytes.length);
        response.setContentType("application/octet-stream; charset=UTF-8");
        IoUtil.write(response.getOutputStream(), false, bytes);
    }


}
