package com.company.project.modules.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.company.project.core.Assert;
import com.company.project.core.Results;
import com.company.project.modules.system.entity.UserNotice;
import com.company.project.modules.system.mapper.UserNoticeMapper;
import com.company.project.modules.system.service.UserNoticeService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户通知 服务实现类
 * </p>
 *
 * @author author
 * @since 2020-09-04
 */
@Service
public class UserNoticeServiceImpl extends ServiceImpl<UserNoticeMapper, UserNotice> implements UserNoticeService {

    @Override
    public UserNotice getByUserId(Integer userId) {
        Assert.requireNonNull(userId, Results.Fail);
        QueryWrapper<UserNotice> queryWrapper = new QueryWrapper<UserNotice>()
                .eq("user_id", userId);
        return this.getOne(queryWrapper);
    }
}
