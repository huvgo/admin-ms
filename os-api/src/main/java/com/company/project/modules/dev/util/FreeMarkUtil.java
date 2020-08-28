package com.company.project.modules.dev.util;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class FreeMarkUtil {

    private final Logger logger = LoggerFactory.getLogger(FreeMarkUtil.class);

    private final Configuration configuration;

    public FreeMarkUtil() {
        configuration = new Configuration(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);
        configuration.setDefaultEncoding("UTF-8");
        configuration.setClassForTemplateLoading(FreeMarkUtil.class, StringPool.SLASH);
    }


    public void writer(Map<String, Object> objectMap, String templatePath, String outputFile) throws IOException, TemplateException {
        Template template = configuration.getTemplate(templatePath);
        try (FileOutputStream fileOutputStream = new FileOutputStream(outputFile)) {
            template.process(objectMap, new OutputStreamWriter(fileOutputStream, StandardCharsets.UTF_8));
        }
        logger.debug("模板:{};    文件:{}", templatePath, outputFile);
    }
}
