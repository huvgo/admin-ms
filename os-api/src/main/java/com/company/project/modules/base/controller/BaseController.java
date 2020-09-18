package com.company.project.modules.base.controller;

import com.company.project.cache.UserCacheUtil;
import com.company.project.modules.system.entity.User;

public abstract class BaseController {

    public User getCurrentLoginUser() {
        return UserCacheUtil.getCurrentUser();
    }

}
