package com.company.project.cache;

import com.company.project.modules.sys.entity.User;

public interface UserCacheService {

    void putUser(String token, User user);

    User getUser(String token);

    void deleteUser(String token);

    void putToken(String key, String token);

    String getToken(String key);

}
