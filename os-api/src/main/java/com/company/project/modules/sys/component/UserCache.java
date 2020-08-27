package com.company.project.modules.sys.component;

import cn.hutool.cache.CacheUtil;
import cn.hutool.cache.impl.TimedCache;
import cn.hutool.core.date.DateUnit;
import com.company.project.modules.sys.entity.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 用户信息缓存
 * 定时缓存，对被缓存的对象定义一个过期时间，当对象超过过期时间会被清理。此缓存没有容量限制，对象只有在过期后才会被移除。
 */
@Slf4j
@Component
public class UserCache {

    private final TimedCache<String, User> userCache;
    private final TimedCache<String, String> tokenCache;


    public UserCache() {
        //创建缓存，默认一小时过期
        userCache = CacheUtil.newTimedCache(DateUnit.HOUR.getMillis());
        tokenCache = CacheUtil.newTimedCache(DateUnit.HOUR.getMillis());
    }

    public synchronized void putUser(String token, User user) throws JsonProcessingException {
        userCache.put(token, user);
    }

    public synchronized User getUser(String token) {
        return userCache.get(token);
    }

    public synchronized void putToken(String key, String token) {
        tokenCache.put(key, token);
    }

    public synchronized String getToken(String key) {
        return tokenCache.get(key);
    }
}
