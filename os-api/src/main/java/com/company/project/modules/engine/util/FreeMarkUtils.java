package com.company.project.modules.engine.util;

import cn.hutool.core.io.IoUtil;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class FreeMarkUtils {

    private final Logger logger = LoggerFactory.getLogger(FreeMarkUtils.class);

    private final Configuration configuration;

    public FreeMarkUtils() {
        configuration = new Configuration(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);
        configuration.setDefaultEncoding("UTF-8");
        configuration.setClassForTemplateLoading(FreeMarkUtils.class, StringPool.SLASH);
    }


    public void writer(Map<String, Object> objectMap, String templatePath, String outputFile) throws IOException, TemplateException {
        Template template = configuration.getTemplate(templatePath);
        try (FileOutputStream fileOutputStream = new FileOutputStream(outputFile)) {
            template.process(objectMap, new OutputStreamWriter(fileOutputStream, StandardCharsets.UTF_8));
        }
        logger.debug("模板:{};    文件:{}", templatePath, outputFile);
    }


    public String writer(Map<String, Object> objectMap, String templatePath) throws IOException, TemplateException {
        Template template = configuration.getTemplate(templatePath);
        StringWriter stringWriter = new StringWriter();
        template.process(objectMap, stringWriter);
        IoUtil.close(stringWriter);
        return stringWriter.toString();
    }
}
