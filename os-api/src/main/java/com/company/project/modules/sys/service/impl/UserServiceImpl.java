package com.company.project.modules.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.company.project.modules.sys.entity.User;
import com.company.project.modules.sys.mapper.UserMapper;
import com.company.project.modules.sys.service.UserService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 系统用户 服务实现类
 * </p>
 *
 * @author root
 * @since 2020-08-12
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Override
    public User getByUsername(String username) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<User>().eq("username", username);

        return this.getOne(queryWrapper);
    }

    @Override
    public User getByUsernameAndPassword(String username, String password) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<User>().eq("username", username).eq("password", password);
        return this.getOne(queryWrapper);
    }
}
