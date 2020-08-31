package com.conpany.project;

import cn.hutool.core.util.IdUtil;
import cn.hutool.crypto.SecureUtil;
import org.junit.jupiter.api.Test;

public class PasswordTest {
    @Test
    public void test() {
        String password = "111111";
        String s = IdUtil.fastSimpleUUID();
        System.out.println("盐 = " + s);
        System.out.println("盐长度 = " + s.length());
        String s1 = SecureUtil.md5().setSalt(s.getBytes()).digestHex(password);
        System.out.println("加密后密码 = " + s1);
        System.out.println("加密后密码长度 = " + s1.length());
    }
}
