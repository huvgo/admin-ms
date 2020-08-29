package com.company.project.modules.dev.service;

import cn.hutool.db.Entity;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.company.project.modules.dev.entity.code.Table;
import freemarker.template.TemplateException;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface CodeService {


    Page<Table> page(Integer currentPage, Integer pageSize, Map<String, Object> params) throws SQLException;


    void generator(Table table, boolean local) throws IOException, TemplateException, SQLException;
}
