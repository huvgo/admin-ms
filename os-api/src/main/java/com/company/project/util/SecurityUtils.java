package com.company.project.util;

import com.company.project.modules.common.service.TokenService;
import com.company.project.modules.system.entity.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * 用户缓存 工具类
 * <p>
 * 该类只能用在http请求中
 */
@Component
public class SecurityUtils {

    private static TokenService tokenService;

    public SecurityUtils(TokenService tokenService) {
        SecurityUtils.tokenService = tokenService;
    }

    /**
     * 通过请求头部的X-Token获取用户信息
     *
     * @return 当前登录用户
     */
    public static User getCurrentUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    /**
     * 获取Authentication
     */
    public static Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }
}
