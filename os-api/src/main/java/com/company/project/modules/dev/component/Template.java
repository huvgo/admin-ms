package com.company.project.modules.dev.component;

public class Template {

    private String templatePath;

    private String directory;

    private String fileSuffix;

    public Template(String templatePath, String directory,String fileSuffix) {
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
