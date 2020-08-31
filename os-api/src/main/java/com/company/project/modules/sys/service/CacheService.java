package com.company.project.modules.sys.service;

import com.company.project.modules.sys.entity.User;

public interface CacheService {

    void putUser(String token, User user);

    User getUser(String token);

    void putToken(String key, String token);

    String getToken(String key);
}
