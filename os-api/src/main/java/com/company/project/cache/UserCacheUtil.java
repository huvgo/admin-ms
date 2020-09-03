package com.company.project.cache;

import com.company.project.modules.sys.entity.User;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

@Component
public class UserCacheUtil {

    private static UserCacheService userCacheService;

    public UserCacheUtil(UserCacheService userCacheService) {
        UserCacheUtil.userCacheService = userCacheService;
    }

    public static User getCurrentUser() {
        HttpServletRequest request = Objects.requireNonNull((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String token = request.getHeader("X-Token");
        return userCacheService.getUser(token);
    }

    public static User getUser(String token) {
        return userCacheService.getUser(token);
    }

}
