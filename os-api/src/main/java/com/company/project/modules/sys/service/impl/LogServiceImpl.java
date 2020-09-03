package com.company.project.modules.sys.service.impl;

import com.company.project.modules.sys.entity.Log;
import com.company.project.modules.sys.mapper.LogMapper;
import com.company.project.modules.sys.service.LogService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * <p>
 * 系统日志 服务实现类
 * </p>
 *
 * @author author
 * @since 2020-09-03
 */
@Service
public class LogServiceImpl extends ServiceImpl<LogMapper, Log> implements LogService {

}
