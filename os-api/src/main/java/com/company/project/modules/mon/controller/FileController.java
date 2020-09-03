package com.company.project.modules.mon.controller;

import com.company.project.core.Result;
import com.company.project.modules.mon.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * 上传 前端控制器
 */
@Slf4j
@RestController
@RequestMapping("/mon/file")
public class FileController {

    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping("/upload")
    public Result<Object> upload(@RequestParam("file") MultipartFile file, @RequestParam(value = "moduleDir") String moduleDir) {
        String path = fileService.upload(file, moduleDir);
        return Result.success(path);
    }


    @PostMapping("/download")
    public Result<Object> download() {
        // todo
        return Result.success();
    }
}
