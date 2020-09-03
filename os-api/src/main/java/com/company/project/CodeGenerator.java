package com.company.project;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class CodeGenerator {

    private static final Logger logger = LoggerFactory.getLogger(CodeGenerator.class);


    public static void main(String[] args) throws Exception {

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
