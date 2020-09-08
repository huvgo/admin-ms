package com.company.project.modules.engine.service.impl;

import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.company.project.modules.engine.entity.code.Table;
import com.company.project.modules.engine.entity.code.Template;
import com.company.project.modules.engine.mapper.TableMapper;
import com.company.project.modules.engine.service.CodeService;
import com.company.project.modules.engine.util.CodeUtils;
import com.company.project.modules.engine.util.FreeMarkUtils;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipOutputStream;

@Slf4j
@Service
public class CodeServiceImpl implements CodeService {

    private final TableMapper tableMapper;

    public CodeServiceImpl(TableMapper tableMapper) {
        this.tableMapper = tableMapper;
    }

    @Override
    public Page<Table> page(Integer currentPage, Integer pageSize, Map<String, Object> params) throws SQLException {
        Page<Table> page = new Page<>();
        page.setRecords(tableMapper.page((currentPage - 1) * pageSize, pageSize, params));
        page.setTotal(tableMapper.total(params));
        return page;
    }

    @Override
    public byte[] generate(List<Table> tableList) throws IOException, TemplateException {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream(); ZipOutputStream zipOut = new ZipOutputStream(baos)) {
            for (Table table : tableList) {
                if (table.isGenerator()) {
                    int parentMenuId = table.getParentMenuId();
                    // todo 生成菜单栏

                    // 为了获取模板类型
                    List<Template> templateList = CodeUtils.getTemplates();

                    // 为了获取填充模板的数据模型
                    Dict dataModel = CodeUtils.getDataModel(table);

                    // 输出模板
                    FreeMarkUtils freeMarkUtils = new FreeMarkUtils();
                    for (Template template : templateList) {
                        String fileName = dataModel.getStr("lowerFirstName");
                        if (template.isUpperFirstName()) {
                            fileName = dataModel.getStr("upperFirstName");
                        }
                        String outputPath = StrUtil.join(File.separator, template.getRootPath(), dataModel.getStr("moduleName")
                                , template.getClassifyPath(), fileName + template.getFileSuffix());
                        String writer = freeMarkUtils.writer(dataModel, template.getTemplatePath());
                        CodeUtils.zip(zipOut, writer.getBytes(), outputPath);
                    }
                }
            }
            zipOut.close();
            baos.close();
            return baos.toByteArray();
        }
    }
}
