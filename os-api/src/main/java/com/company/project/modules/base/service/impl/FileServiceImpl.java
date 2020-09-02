package com.company.project.modules.base.service.impl;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import com.company.project.core.Assert;
import com.company.project.core.ServiceException;
import com.company.project.modules.base.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ClassUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Slf4j
@Service
public class FileServiceImpl implements FileService {

    @Value("${admin-os.domain}")
    private String domain;

    public static final String CLASS_PATH = ClassUtils.getDefaultClassLoader().getResource("").getPath();

    @Override
    public String upload(MultipartFile file, String moduleDir) {
        Assert.requireTrue(!file.isEmpty(), "文件上传失败！");
        String rootPath = CLASS_PATH + "static";
        String today = DateUtil.format(new DateTime(), "yyyyMMdd");
        String name = IdUtil.fastSimpleUUID();
        String suffix = FileUtil.getSuffix(file.getOriginalFilename());
        String relativePath = "/" + moduleDir + "/" + today + "/" + name + "." + suffix;
        // 目录为: classPath路径/static/moduleDir/今天日期/随机文件名
        File local = new File(rootPath, relativePath);
        FileUtil.mkParentDirs(local);
        try {
            file.transferTo(local);
        } catch (IOException e) {
            log.error("文件上传失败", e);
            throw new ServiceException("文件上传失败");
        }
        String url = domain + relativePath;
        return url;
    }

}
