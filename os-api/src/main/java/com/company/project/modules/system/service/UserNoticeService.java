package com.company.project.modules.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.company.project.modules.system.entity.Notice;
import com.company.project.modules.system.entity.User;
import com.company.project.modules.system.entity.UserNotice;

import java.util.List;

/**
 * <p>
 * 用户通知 服务类
 * </p>
 *
 * @author author
 * @since 2020-09-04
 */
public interface UserNoticeService extends IService<UserNotice> {

    UserNotice getByUserId(Integer id);

    List<Notice> getByUser(User user);
}
