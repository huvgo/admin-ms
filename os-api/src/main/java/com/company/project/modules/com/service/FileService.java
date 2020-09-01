package com.company.project.modules.com.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    String upload(MultipartFile file, String moduleDir);
}
