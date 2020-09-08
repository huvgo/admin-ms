package com.company.project.modules.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.company.project.modules.system.entity.Notice;
import com.company.project.modules.system.mapper.NoticeMapper;
import com.company.project.modules.system.service.NoticeService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 系统通知 服务实现类
 * </p>
 *
 * @author author
 * @since 2020-09-02
 */
@Service
public class NoticeServiceImpl extends ServiceImpl<NoticeMapper, Notice> implements NoticeService {

}
