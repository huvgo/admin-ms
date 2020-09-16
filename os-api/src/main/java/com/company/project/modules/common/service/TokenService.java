package com.company.project.modules.common.service;

import com.company.project.modules.system.entity.User;

import javax.servlet.http.HttpServletRequest;

public interface TokenService {

    User getCurrentLoginUser(HttpServletRequest request);
}
