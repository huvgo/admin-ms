package com.company.project.modules.common.service;

import com.company.project.modules.system.entity.User;

import javax.servlet.http.HttpServletRequest;

/**
 * 用户信息缓存 接口
 */
public interface TokenService {

    void putUser(String token, User user);

    User getUser(String token);

    void deleteUser(String token);

    void putToken(String key, String token);

    String getToken(String key);

    default User getUser(HttpServletRequest request) {
        String token = request.getHeader("X-Token");
        if (token == null) {
            return null;
        }
        return this.getUser(token);
    }
}
