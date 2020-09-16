package com.company.project.modules.common.service.impl;

import com.company.project.modules.common.service.UserCacheService;
import com.company.project.modules.common.service.TokenService;
import com.company.project.modules.system.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
public class TokenServiceImpl implements TokenService {
    @Autowired
    private UserCacheService userCacheService;

    @Override
    public User getCurrentLoginUser(HttpServletRequest request) {
        String token = request.getHeader("X-Token");
        if (token == null) {
            return null;
        }
        User user = userCacheService.getUser(token);
        return user;
    }
}
