package com.company.project.util;

import com.company.project.core.Assert;
import com.company.project.core.ResultCode;
import com.company.project.modules.sys.entity.User;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 *
 */
public class AdminOSUtil {

    public static User getCurrentUser(){
        HttpServletRequest request = Objects.requireNonNull((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String token = request.getHeader("X-Token");
        User user = UserCache.getUser(token);
        return user;
    }
}
