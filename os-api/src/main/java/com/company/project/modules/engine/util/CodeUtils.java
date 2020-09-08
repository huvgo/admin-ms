package com.company.project.modules.engine.util;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.StrUtil;
import cn.hutool.setting.dialect.Props;
import com.company.project.modules.engine.entity.code.Table;
import com.company.project.modules.engine.entity.code.Template;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.zip.Deflater;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Slf4j
public class CodeUtils {

    private static final Props props = new Props("code-generator.properties");
    public static final String author = props.getStr("author");
    public static final String projectFolder = props.getStr("project.folder");
    public static final String javaProjectName = props.getStr("java.project.name");
    public static final String javaPackageName = props.getStr("java.package.name");
    public static final String vueProjectName = props.getStr("vue.project.name");

    private static final List<Template> templates = new ArrayList<>();

    static {
        // admin-os/os-api/src/main/java/com/company/project
        String rootPath = StrUtil.join(File.separator
                , CodeUtils.projectFolder, CodeUtils.javaProjectName, "src", "main", "java"
                , CodeUtils.javaPackageName.split("\\."));
        templates.add(new Template("/templates/controller.java.ftl", rootPath, "controller", "Controller.java", true));
        templates.add(new Template("/templates/entity.java.ftl", rootPath, "entity", ".java", true));
        templates.add(new Template("/templates/service.java.ftl", rootPath, "service", "Service.java", true));
        templates.add(new Template("/templates/serviceImpl.java.ftl", rootPath, "service/impl", "ServiceImpl.java", true));
        templates.add(new Template("/templates/mapper.java.ftl", rootPath, "mapper", "Mapper.java", true));
        templates.add(new Template("/templates/mapper.xml.ftl", rootPath, "mapper/xml", "Mapper.xml", true));
        // admin-os/os-vue/src
        rootPath = StrUtil.join(File.separator
                , CodeUtils.projectFolder, CodeUtils.vueProjectName, "src");

        templates.add(new Template("/templates/page.js.ftl", String.join(File.separator, rootPath, "api"), "", ".js", false));
        templates.add(new Template("/templates/page.vue.ftl", String.join(File.separator, rootPath, "views", "modules"), "", ".vue", false));
    }

    public static List<Template> getTemplates() {
        return templates;
    }


    private static final HashMap<String, String> jdbcType2JavaTypeMap = new HashMap<>();

    private static final String STRING = "String";
    private static final String INTEGER = "Integer";

    static {
        jdbcType2JavaTypeMap.put("tinyint", INTEGER);
        jdbcType2JavaTypeMap.put("smallint", INTEGER);
        jdbcType2JavaTypeMap.put("mediumint", INTEGER);
        jdbcType2JavaTypeMap.put("int", INTEGER);
        jdbcType2JavaTypeMap.put(INTEGER, INTEGER);
        jdbcType2JavaTypeMap.put("bigint", "Long");
        jdbcType2JavaTypeMap.put("float", "Float");
        jdbcType2JavaTypeMap.put("double", "Double");
        jdbcType2JavaTypeMap.put("decimal", "BigDecimal");
        jdbcType2JavaTypeMap.put("bit", "Boolean");


        jdbcType2JavaTypeMap.put("char", STRING);
        jdbcType2JavaTypeMap.put("varchar", STRING);
        jdbcType2JavaTypeMap.put("tinytext", STRING);
        jdbcType2JavaTypeMap.put("text", STRING);
        jdbcType2JavaTypeMap.put("mediumtext", STRING);
        jdbcType2JavaTypeMap.put("longtext", STRING);
        jdbcType2JavaTypeMap.put("json", STRING);

        jdbcType2JavaTypeMap.put("date", "Date");
        jdbcType2JavaTypeMap.put("datetime", "Date");
        jdbcType2JavaTypeMap.put("timestamp", "Date");
    }

    public static String jdbcType2JavaType(String jdbcType) {
        return jdbcType2JavaTypeMap.get(jdbcType);
    }

    public static Dict getDataModel(Table table) {
        // 整理模板需要的数据
        Dict dataModel = new Dict();

        String tableName = table.getName();
        // 表前缀为模块名称
        int i = tableName.indexOf("_");
        String moduleName = tableName.substring(0, i);
        // 移除表前缀 转换驼峰实体名称
        String fileName = StrUtil.toCamelCase(tableName.substring(i));

        dataModel.put("fields", table.getColumns());
        dataModel.put("table", table);
        dataModel.put("moduleName", moduleName);
        dataModel.put("lowerFirstName", StrUtil.lowerFirst(fileName));
        dataModel.put("upperFirstName", StrUtil.upperFirst(fileName));
        dataModel.put("package", CodeUtils.javaPackageName + "." + moduleName);
        dataModel.put("author", CodeUtils.author);
        dataModel.put("date", DateUtil.date().toDateStr());

        return dataModel;
    }


    /**
     * 把字节信息写入到zip输出流
     *
     * @param zipOut zip输出流
     * @param o      字节数组
     * @param path   压缩路径
     * @throws IOException IOException
     */
    public static void zip(ZipOutputStream zipOut, byte[] o, String path) throws IOException {
        // 1.将需要压缩的字节输出流，转为字节数组输入流，
        ByteArrayInputStream bais = new ByteArrayInputStream(o);
        // 2.创建字节数组输出流，用于返回压缩后的输出流字节数组

        // 进行压缩存储
        zipOut.setMethod(ZipOutputStream.DEFLATED);
        // 压缩级别值为0-9共10个级别(值越大，表示压缩越厉害)
        zipOut.setLevel(Deflater.BEST_COMPRESSION);
        //4.设置ZipEntry对象，并对需要压缩的文件命名
        zipOut.putNextEntry(new ZipEntry(path));
        //5.读取要压缩的字节输出流，进行压缩
        int temp = 0;
        while ((temp = bais.read()) != -1) {
            // 压缩输出
            zipOut.write(temp);
            zipOut.flush();
        }
        bais.close();
    }
}
