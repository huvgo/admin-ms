package com.company.project.modules.mon.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    String upload(MultipartFile file, String moduleDir);
}
