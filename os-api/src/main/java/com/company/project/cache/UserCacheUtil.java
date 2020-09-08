package com.company.project.cache;

import com.company.project.modules.system.entity.User;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * 用户缓存 工具类
 * <p>
 * 该类只能用在http请求中
 */
@Component
public class UserCacheUtil {

    private static UserCache userCache;

    public UserCacheUtil(UserCache userCache) {
        UserCacheUtil.userCache = userCache;
    }

    /**
     * 通过请求头部的X-Token获取用户信息
     *
     * @return 当前登录用户
     */
    public static User getCurrentUser() {
        HttpServletRequest request = Objects.requireNonNull((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String token = request.getHeader("X-Token");
        return userCache.getUser(token);
    }

    public static User getUser(String token) {
        return userCache.getUser(token);
    }

}
