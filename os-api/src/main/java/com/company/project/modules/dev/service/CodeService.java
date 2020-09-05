package com.company.project.modules.dev.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.company.project.modules.dev.entity.code.Table;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface CodeService {


    Page<Table> page(Integer currentPage, Integer pageSize, Map<String, Object> params) throws SQLException;


    byte[] generate(List<Table> tableList) throws Exception;
}
