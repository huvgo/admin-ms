package com.company.project.modules.dev.controller;

import com.company.project.core.Result;
import com.company.project.modules.dev.entity.oshi.SystemHardwareInfo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 系统监控 前端控制器
 * </p>
 *
 * @author root
 */
@RestController
@RequestMapping("/monitor/oshi")
public class OshiController {

    @GetMapping
    public Result<SystemHardwareInfo> detail() {
        SystemHardwareInfo systemHardwareInfo = new SystemHardwareInfo();
        systemHardwareInfo.copyTo();
        return Result.success(systemHardwareInfo);
    }
}
