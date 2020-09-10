package com.company.project.modules.system.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.company.project.core.Assert;
import com.company.project.core.Results;
import com.company.project.modules.base.entity.BaseEntity;
import com.company.project.modules.system.entity.Notice;
import com.company.project.modules.system.entity.User;
import com.company.project.modules.system.entity.UserNotice;
import com.company.project.modules.system.mapper.UserNoticeMapper;
import com.company.project.modules.system.service.NoticeService;
import com.company.project.modules.system.service.UserNoticeService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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

    private final NoticeService noticeService;

    public UserNoticeServiceImpl(NoticeService noticeService) {
        this.noticeService = noticeService;
    }

    @Override
    public UserNotice getByUserId(Integer userId) {
        Assert.requireNonNull(userId, Results.Fail);
        QueryWrapper<UserNotice> queryWrapper = new QueryWrapper<UserNotice>()
                .eq("user_id", userId);
        return this.getOne(queryWrapper);
    }

    @Override
    public List<Notice> getByUser(User user) {
        Date now = new Date();
        // 用户上次获取消息的时间,如果没有则默认为创建用户的时间
        Date updateDate = user.getCreateTime();

        // 为了获取用户上次从system_notice表拉取的消息
        UserNotice userNotice = this.getByUserId(user.getId());
        // 构造查询条件
        QueryWrapper<Notice> queryWrapper = new QueryWrapper<>();
        UserNotice finalUserNotice = userNotice;
        if (userNotice != null) {
            // 为了把用户上次拉取的未读消息也要查询出来
            updateDate = userNotice.getUpdateTime();
            Date finalUpdateDate = updateDate;
            queryWrapper.eq("is_enabled", true).and(c -> c.in(CollUtil.isNotEmpty(finalUserNotice.getNoticeIds()), "id", finalUserNotice.getNoticeIds()).or().gt("push_time", finalUpdateDate).le("push_time", now));
        } else {
            queryWrapper.eq("is_enabled", true).gt("push_time", updateDate).le("push_time", now);
        }
        List<Notice> list = noticeService.list(queryWrapper);


        // 更新用户通知信息表
        if (userNotice == null) {
            userNotice = new UserNotice();
        }
        userNotice.setUserId(user.getId());
        userNotice.setUpdateTime(now);
        List<Integer> noticeIds = list.stream().map(BaseEntity::getId).collect(Collectors.toList());
        userNotice.setNoticeIds(noticeIds);
        this.saveOrUpdate(userNotice);

        return list;
    }
}
