package com.company.project.modules.base.controller;

import com.company.project.modules.system.entity.User;
import com.company.project.util.SecurityUtils;

public abstract class BaseController {

    public User getCurrentLoginUser() {
        return SecurityUtils.getCurrentUser();
    }

}
