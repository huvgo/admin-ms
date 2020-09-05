package com.company.project.modules.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.company.project.modules.sys.entity.User;
import org.springframework.web.multipart.MultipartFile;

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

    User login(String username, String password);

    void encodePassword(User user);

    String upload(MultipartFile file, String moduleDir);
}
