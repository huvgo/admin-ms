package com.company.project.modules.dev.service;

import cn.hutool.db.Entity;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.company.project.modules.dev.entity.oshi.Template;
import freemarker.template.TemplateException;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface CodeService {

    Page<Entity> page(Integer currentPage, Integer pageSize, Map<String, Object> params) throws SQLException;


    void generator(List<Template> templateList, String tableName, boolean local) throws IOException, TemplateException, SQLException;
}
