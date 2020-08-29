package com.company.project.modules.dev.controller;

import cn.hutool.db.Entity;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.company.project.common.annotation.Permissions;
import com.company.project.core.Result;
import com.company.project.modules.dev.entity.oshi.Template;
import com.company.project.modules.dev.mapper.TableMapper;
import com.company.project.modules.dev.service.CodeService;
import com.company.project.modules.dev.service.impl.CodeServiceImpl;
import freemarker.template.TemplateException;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
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
    public Result<Page<Entity>> get(@RequestParam(defaultValue = "1") Integer currentPage, @RequestParam(defaultValue = "10") Integer pageSize, @RequestParam Map<String, Object> params) throws SQLException {
        Page<Entity> page = codeService.page(currentPage, pageSize, params);
        return Result.success(page);
    }

    @PostMapping("/generator")
    public Result<Object> generator(Map<String, Object> params) throws SQLException, IOException, TemplateException {
        String tableName = (String) params.get("tableName");
        List<Template> templateList = new ArrayList<>();
        templateList.add(new Template("/templates/controller.java.ftl", "controller", "Controller.java"));
        templateList.add(new Template("/templates/entity.java.ftl", "entity", ".java"));
        templateList.add(new Template("/templates/service.java.ftl", "service", "Service.java"));
        templateList.add(new Template("/templates/serviceImpl.java.ftl", "service" + File.separator + "impl", "ServiceImpl.java"));
        templateList.add(new Template("/templates/mapper.java.ftl", "mapper", "Mapper.java"));
        templateList.add(new Template("/templates/mapper.xml.ftl", "mapper" + File.separator + "xml", "Mapper.xml"));

        templateList.add(new Template("/templates/page.js.ftl", "abs:C:\\Users\\HuWei\\Github Projects\\admin-os\\os-vue\\src\\api", ".js"));
        templateList.add(new Template("/templates/page.vue.ftl", "abs:C:\\Users\\HuWei\\Github Projects\\admin-os\\os-vue\\src\\views\\modules", ".vue"));

        CodeService codeService = new CodeServiceImpl(new TableMapper());
        codeService.generator(templateList, tableName, true);

        return Result.success();
    }

}
