package com.company.project.modules.com.controller;

import com.company.project.core.Result;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 上传 前端控制器
 */
@RestController
@RequestMapping("/com/file")
public class FileController {

    @PostMapping("/upload")
    public Result<Object> upload() {
        // todo
        return Result.success();
    }


    @PostMapping("/download")
    public Result<Object> download() {
        // todo
        return Result.success();
    }
}
