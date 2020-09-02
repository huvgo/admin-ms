package com.company.project.modules.sys.service.impl;

import com.company.project.modules.sys.entity.Notification;
import com.company.project.modules.sys.mapper.NotificationMapper;
import com.company.project.modules.sys.service.NotificationService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * <p>
 * 系统通知 服务实现类
 * </p>
 *
 * @author author
 * @since 2020-09-02
 */
@Service
public class NotificationServiceImpl extends ServiceImpl<NotificationMapper, Notification> implements NotificationService {

}
