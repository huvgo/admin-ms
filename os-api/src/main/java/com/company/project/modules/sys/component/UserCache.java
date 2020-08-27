package com.company.project.modules.sys.component;

import cn.hutool.cache.CacheUtil;
import cn.hutool.cache.impl.TimedCache;
import cn.hutool.core.date.DateUnit;
import com.company.project.modules.sys.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 用户信息缓存
 * 定时缓存，对被缓存的对象定义一个过期时间，当对象超过过期时间会被清理。此缓存没有容量限制，对象只有在过期后才会被移除。
 */
@Slf4j
@Component
public class UserCache {

    private final TimedCache<String, User> userTimedCache;
    private final TimedCache<String, String> timedCache;


    public UserCache() {
        //创建缓存，默认一小时过期
        userTimedCache = CacheUtil.newTimedCache(DateUnit.HOUR.getMillis());
        timedCache = CacheUtil.newTimedCache(DateUnit.HOUR.getMillis());
    }

    public synchronized void putUser(String token, User user) {
        userTimedCache.put(token, user);
    }

    public synchronized User getUser(String token) {
        return userTimedCache.get(token);
    }

    public synchronized void putToken(String key, String token) {
        timedCache.put(key, token);
    }

    public synchronized String getToken(String key) {
        return timedCache.get(key);
    }
}
