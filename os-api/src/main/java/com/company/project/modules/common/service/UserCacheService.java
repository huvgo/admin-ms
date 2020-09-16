package com.company.project.modules.common.service;

import com.company.project.modules.system.entity.User;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * 用户信息缓存 接口
 */
public interface UserCacheService {

    void putUser(String token, User user);

    User getUser(String token);

    void deleteUser(String token);

    void putToken(String key, String token);

    String getToken(String key);

    default User getCurrentUser() {
        /**
         * 通过请求头部的X-Token获取用户信息
         *
         * @return 当前登录用户
         */
        HttpServletRequest request = Objects.requireNonNull((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String token = request.getHeader("X-Token");
        return getUser(token);
    }
}
