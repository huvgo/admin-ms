package com.company.project;

import com.company.project.modules.dev.entity.oshi.Template;
import com.company.project.modules.dev.mapper.TableMapper;
import com.company.project.modules.dev.service.CodeService;
import com.company.project.modules.dev.service.impl.CodeServiceImpl;
import freemarker.template.TemplateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CodeGenerator {

    private static final Logger logger = LoggerFactory.getLogger(CodeGenerator.class);


    public static void main(String[] args) throws Exception {
        test();
    }

    public static void test() throws TemplateException, IOException, SQLException {
        String moduleName = "sys";
        String tableName = "sys_dept";
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

        open(CodeServiceImpl.ABSOLUTE_OUTPUT_PATH);
    }

    /**
     * 打开输出目录
     */
    private static void open(String outDir) {
        try {
            String osName = System.getProperty("os.name");
            if (osName != null) {
                if (osName.contains("Mac")) {
                    Runtime.getRuntime().exec("open " + outDir);
                } else if (osName.contains("Windows")) {
                    Runtime.getRuntime().exec("cmd /c start explorer \"" + outDir + "\"");
                } else {
                    logger.debug("文件输出目录:" + outDir);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
