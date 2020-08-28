package com.company.project.modules.dev.service.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.company.project.modules.dev.component.FreemarkerTemplateEngine;
import com.company.project.modules.dev.component.Template;
import com.company.project.modules.dev.service.CodeService;
import com.company.project.modules.dev.util.FreeMarkUtil;
import freemarker.template.TemplateException;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CodeServiceImpl implements CodeService {

    // 包名
    private static final String packageName = "com.company.project.modules";

    private static final String AUTHOR = "author";

    // 文件输出路径 相对路径 eg. src\main\java\com\company\project\modules
    private static final String RELATIVE_OUTPUT_PATH = File.separator + "src" + File.separator + "main" + File.separator + "java" + File.separator + packageName.replaceAll("\\.", StringPool.BACK_SLASH + File.separator);

    // 代码输出路径 绝对路径 eg. C:\admin-os\os-api\src\main\java\com\company\project\modules
    private static final String ABSOLUTE_OUTPUT_PATH = System.getProperty("user.dir") + RELATIVE_OUTPUT_PATH;

    public void generator(List<Template> templateList, String tableName, boolean local) throws IOException, TemplateException {
        // 整理模板需要的数据
        Map<String, Object> objectMap = null;

        // 处理名称
        int i = tableName.indexOf("_");
        String moduleNameDir = tableName.substring(0, i);
        String fileName = StrUtil.toCamelCase(tableName.substring(i));


        // 输出模板
        FreeMarkUtil freeMarkUtil = new FreeMarkUtil();
        for (Template template : templateList) {
            String outputFileDir = ABSOLUTE_OUTPUT_PATH + File.separator + moduleNameDir + File.separator + template.getDirectory();
            FileUtil.mkdir(outputFileDir);
            String fileSuffix = template.getFileSuffix();
            String sub = StrUtil.sub(fileSuffix, fileSuffix.lastIndexOf(".") + 1, fileSuffix.length());
            if ("java".equals(sub) || "xml".equals(sub)) {
                fileName = StrUtil.upperFirst(fileName);
            }
            String outputFilePath = outputFileDir + File.separator + fileName;

            freeMarkUtil.writer(objectMap, template.getTemplatePath(), outputFilePath);
        }
    }


}
