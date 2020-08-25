package com.company.project.modules.sys.service.impl;

import com.company.project.modules.sys.entity.User;
import com.company.project.modules.sys.mapper.UserMapper;
import com.company.project.modules.sys.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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

}
