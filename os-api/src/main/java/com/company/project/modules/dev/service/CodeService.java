package com.company.project.modules.dev.service;

import com.company.project.modules.dev.component.Template;
import freemarker.template.TemplateException;

import java.io.IOException;
import java.util.List;

public interface CodeService {

    void generator(List<Template> templateList, String tableName, boolean local) throws IOException, TemplateException;
}
