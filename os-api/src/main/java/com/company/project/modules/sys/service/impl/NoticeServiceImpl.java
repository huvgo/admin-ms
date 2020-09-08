package com.company.project.modules.sys.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.company.project.modules.sys.entity.Notice;
import com.company.project.modules.sys.mapper.NoticeMapper;
import com.company.project.modules.sys.service.NoticeService;
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
