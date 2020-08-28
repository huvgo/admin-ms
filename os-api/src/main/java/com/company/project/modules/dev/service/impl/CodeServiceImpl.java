package com.company.project.modules.dev.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.db.Entity;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.company.project.modules.dev.entity.oshi.Template;
import com.company.project.modules.dev.mapper.TableMapper;
import com.company.project.modules.dev.service.CodeService;
import com.company.project.modules.dev.util.FreeMarkUtil;
import freemarker.template.TemplateException;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CodeServiceImpl implements CodeService {
    private final TableMapper tableMapper;
    // 包名
    private static final String packageName = "com.company.project.modules";

    private static final String AUTHOR = "author";

    // 文件输出路径 相对路径 eg. src\main\java\com\company\project\modules
    private static final String RELATIVE_OUTPUT_PATH = File.separator + "src" + File.separator + "main" + File.separator + "java" + File.separator + packageName.replaceAll("\\.", StringPool.BACK_SLASH + File.separator);
    // 代码输出路径 绝对路径 eg. C:\admin-os\os-api\src\main\java\com\company\project\modules
    public static final String ABSOLUTE_OUTPUT_PATH = System.getProperty("user.dir") + RELATIVE_OUTPUT_PATH;

    public CodeServiceImpl(TableMapper tableMapper) {

        this.tableMapper = tableMapper;
    }

    @Override
    public Page<Entity> page(Integer currentPage, Integer pageSize, Map<String, Object> params) throws SQLException {
        Page<Entity> page = new Page<>();
        page.setRecords(tableMapper.page((currentPage - 1) * pageSize, pageSize, params));
        page.setTotal(tableMapper.total(params));
        return page;
    }

    public void generator(List<Template> templateList, String tableName, boolean local) throws IOException, TemplateException, SQLException {
        // 表前缀为模块名称
        int i = tableName.indexOf("_");
        String moduleName = tableName.substring(0, i);
        // 移除表前缀 转换驼峰实体名称
        String fileName = StrUtil.toCamelCase(tableName.substring(i));

        // 整理模板需要的数据
        Map<String, Object> objectMap = new HashMap<>();
        List<Entity> fields = tableMapper.queryColumns(tableName);
        objectMap.put("fields", fields);
        Entity table = tableMapper.queryTable(tableName);
        objectMap.put("table", table);
        objectMap.put("moduleName", moduleName);
        objectMap.put("lowerFirstName", StrUtil.lowerFirst(fileName));
        objectMap.put("upperFirstName", StrUtil.upperFirst(fileName));
        objectMap.put("package", packageName + "." + moduleName);
        objectMap.put("author", AUTHOR);
        objectMap.put("date", DateUtil.date().toDateStr());

        // 输出模板
        FreeMarkUtil freeMarkUtil = new FreeMarkUtil();
        for (Template template : templateList) {
            String outputFileDir;
            if (template.getDirectory().startsWith("abs:")) {
                outputFileDir = StrUtil.removePrefix(template.getDirectory(), "abs:") + File.separator + moduleName;
            } else {
                outputFileDir = ABSOLUTE_OUTPUT_PATH + File.separator + moduleName + File.separator + template.getDirectory();
            }
            FileUtil.mkdir(outputFileDir);
            String fileSuffix = template.getFileSuffix();
            String sub = StrUtil.sub(fileSuffix, fileSuffix.lastIndexOf(".") + 1, fileSuffix.length());
            if ("java".equals(sub) || "xml".equals(sub)) {
                fileName = StrUtil.upperFirst(fileName);
            } else {
                fileName = StrUtil.lowerFirst(fileName);
            }
            String outputFilePath = outputFileDir + File.separator + fileName + template.getFileSuffix();
            freeMarkUtil.writer(objectMap, template.getTemplatePath(), outputFilePath);
        }
    }


}
