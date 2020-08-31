package com.company.project.modules.sys.entity;

import cn.hutool.core.util.StrUtil;

public class Token {

    private String token;

    public String orElseGet(String token) {
        return StrUtil.isNotBlank(this.token) ? this.token : token;
    }

}
