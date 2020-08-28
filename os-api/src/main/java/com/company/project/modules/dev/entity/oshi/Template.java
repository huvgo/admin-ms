package com.company.project.modules.dev.entity.oshi;

/**
 * 页面模板
 */
public class Template {

    /**
     * 模板相对路径
     */
    private String templatePath;

    /**
     * 目标文件夹
     */
    private String directory;

    /**
     * 文件后缀名
     */
    private String fileSuffix;

    public Template(String templatePath, String directory, String fileSuffix) {
        this.templatePath = templatePath;
        this.directory = directory;
        this.fileSuffix = fileSuffix;
    }

    public String getTemplatePath() {
        return templatePath;
    }

    public void setTemplatePath(String templatePath) {
        this.templatePath = templatePath;
    }

    public String getDirectory() {
        return directory;
    }

    public void setDirectory(String directory) {
        this.directory = directory;
    }

    public String getFileSuffix() {
        return fileSuffix;
    }

    public void setFileSuffix(String fileSuffix) {
        this.fileSuffix = fileSuffix;
    }
}
