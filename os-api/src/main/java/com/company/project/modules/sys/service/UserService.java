package com.company.project.modules.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.company.project.modules.sys.entity.User;

/**
 * <p>
 * 系统用户 服务类
 * </p>
 *
 * @author root
 * @since 2020-08-12
 */
public interface UserService extends IService<User> {

    User getByUsername(String username);

    User getByUsernameAndPassword(String username, String password);
}
