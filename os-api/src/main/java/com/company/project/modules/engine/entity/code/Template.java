package com.company.project.modules.engine.entity.code;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 页面模板
 */
@Data
@AllArgsConstructor
public class Template {

    /**
     * 模板相对路径
     */
    private String templatePath;

    /**
     * 相对根路径
     */
    private String rootPath;
    /**
     * 文件分类文件夹
     */
    private String classifyPath;

    /**
     * 文件名补充
     */
    private String fileSuffix;

    /**
     * 文件名称大写首字母
     */
    private boolean upperFirstName;

}
