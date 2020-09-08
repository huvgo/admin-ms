package com.company.project.cache;

import com.company.project.modules.system.entity.User;

/**
 * 用户信息缓存 接口
 */
public interface UserCache {

    void putUser(String token, User user);

    User getUser(String token);

    void deleteUser(String token);

    void putToken(String key, String token);

    String getToken(String key);

}
