package com.company.project.modules.dev.component;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.db.Entity;
import cn.hutool.db.handler.EntityListHandler;
import cn.hutool.db.sql.SqlExecutor;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.company.project.util.JDBCUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.util.*;

public class CodeGenerator {

    private static final Logger logger = LoggerFactory.getLogger(CodeGenerator.class);

    // 包名
    private static final String packageName = "com.company.project.modules";
    private static final String author = "root";

    // 代码输出路径 eg. C:\admin-os\os-api\src\main\java\com\company\project\modules
    private static final String OUTPUT_PATH = System.getProperty("user.dir") + File.separator + "src" + File.separator + "main" + File.separator + "java" + File.separator + packageName.replaceAll("\\.", StringPool.BACK_SLASH + File.separator);


    public static void main(String[] args) throws Exception {
        String moduleName = "sys";
        String tableName = "sys_dictionary";
        List<Template> templateList = new ArrayList<>();
        templateList.add(new Template("/templates/controller.java.ftl", "controller", "Controller.java"));
        templateList.add(new Template("/templates/entity.java.ftl", "entity", ".java"));
        templateList.add(new Template("/templates/service.java.ftl", "service", "Service.java"));
        templateList.add(new Template("/templates/serviceImpl.java.ftl", "service" + File.separator + "impl", "ServiceImpl.java"));
        templateList.add(new Template("/templates/mapper.java.ftl", "mapper", "Mapper.java"));
        templateList.add(new Template("/templates/mapper.xml.ftl", "mapper" + File.separator + "xml", "Mapper.xml"));

/*
        templateList.add(new Template("/templates/page.vue.ftl", "vue", ".vue"));
        templateList.add(new Template("/templates/page.js.ftl", "vue", ".vue"));
*/

        CodeGenerator generator = new CodeGenerator();
        generator.generator(moduleName, tableName, templateList);

        // 打开输出目录
        open(OUTPUT_PATH + File.separator + moduleName);

    }

    private void generator(String moduleName, String tableName, List<Template> templateList) throws Exception {
        // 查询表的字段信息
        Connection conn = JDBCUtils.getConnection();
        List<Entity> tableFields = SqlExecutor.query(conn, "show full fields from " + tableName, new EntityListHandler());

        // 整理模板需要的数据
        Map<String, Object> objectMap = genTemplateParams(tableFields, moduleName, tableName);

        // 输出模板
        FreemarkerTemplateEngine freemarkerTemplateEngine = new FreemarkerTemplateEngine();
        for (Template template : templateList) {
            String outputFilePath = OUTPUT_PATH + File.separator + moduleName + File.separator + template.getDirectory() + File.separator + objectMap.get("upperFirstName") + template.getFileSuffix();
            freemarkerTemplateEngine.writer(objectMap, template.getTemplatePath(), outputFilePath);
        }
    }

    private Map<String, Object> genTemplateParams(List<Entity> tableFields, String moduleName, String tableName) {
        Map<String, Object> objectMap = new HashMap<>();
        ArrayList<Object> table = CollectionUtil.newArrayList();
        tableFields.forEach(entity -> {
            HashMap<String, Object> row = CollectionUtil.newHashMap();
            Set<Map.Entry<String, Object>> entries = entity.entrySet();
            entries.forEach(e -> {
                String key = StrUtil.lowerFirst(e.getKey());
                String value;
                if ("field".equals(key)) {
                    value = StrUtil.toCamelCase((String) e.getValue());
                } else {
                    value = (String) e.getValue();
                }
                row.put(StrUtil.lowerFirst(key), value);
            });

            String jdbcTypeAndLength = (String) row.get("type");
            String jdbcType = jdbcTypeAndLength.split("\\(")[0];
            row.put("javaType", jdbcType2JavaTypeMap.get(jdbcType));
            table.add(row);
        });
        String name = StrUtil.toCamelCase(StrUtil.removePrefix(tableName, moduleName));
        objectMap.put("table", table);
        objectMap.put("tableComment", "数据字典");
        objectMap.put("package", packageName + "." + moduleName);
        objectMap.put("moduleName", moduleName);
        objectMap.put("lowerFirstName", StrUtil.lowerFirst(name));
        objectMap.put("upperFirstName", StrUtil.upperFirst(name));
        objectMap.put("author", author);
        objectMap.put("date", DateUtil.date().toDateStr());
        logger.info(JSONUtil.parse(tableFields).toStringPretty());
        return objectMap;
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

    private static HashMap<String, String> jdbcType2JavaTypeMap = new HashMap<>();

    static {
        jdbcType2JavaTypeMap.put("tinyint", "Integer");
        jdbcType2JavaTypeMap.put("smallint", "Integer");
        jdbcType2JavaTypeMap.put("mediumint", "Integer");
        jdbcType2JavaTypeMap.put("int", "Integer");
        jdbcType2JavaTypeMap.put("integer", "Integer");
        jdbcType2JavaTypeMap.put("bigint", "Long");
        jdbcType2JavaTypeMap.put("float", "Float");
        jdbcType2JavaTypeMap.put("double", "Double");
        jdbcType2JavaTypeMap.put("decimal", "BigDecimal");
        jdbcType2JavaTypeMap.put("bit", "Boolean");

        jdbcType2JavaTypeMap.put("char", "String");
        jdbcType2JavaTypeMap.put("varchar", "String");
        jdbcType2JavaTypeMap.put("tinytext", "String");
        jdbcType2JavaTypeMap.put("text", "String");
        jdbcType2JavaTypeMap.put("mediumtext", "String");
        jdbcType2JavaTypeMap.put("longtext", "String");

        jdbcType2JavaTypeMap.put("date", "Date");
        jdbcType2JavaTypeMap.put("datetime", "Date");
        jdbcType2JavaTypeMap.put("timestamp", "Date");
    }
}
