package com.company.project.cache;

import cn.hutool.cache.CacheUtil;
import cn.hutool.cache.impl.TimedCache;
import cn.hutool.core.date.DateUnit;
import com.company.project.modules.system.entity.User;

/**
 * 用户信息缓存接口的本地缓存实现类
 * 定时缓存，对被缓存的对象定义一个过期时间，当对象超过过期时间会被清理。此缓存没有容量限制，对象只有在过期后才会被移除。
 */
public class LocalUserCache implements UserCache {

    //创建缓存，默认一小时过期
    private static final TimedCache<String, User> userCache = CacheUtil.newTimedCache(DateUnit.HOUR.getMillis());
    private static final TimedCache<String, String> tokenCache = CacheUtil.newTimedCache(DateUnit.HOUR.getMillis());

    public synchronized void putUser(String token, User user) {
        userCache.put(token, user);
    }

    public synchronized User getUser(String token) {
        return userCache.get(token);
    }

    @Override
    public synchronized void deleteUser(String token) {
        userCache.remove(token);
    }

    public synchronized void putToken(String key, String token) {
        tokenCache.put(key, token);
    }

    public synchronized String getToken(String key) {
        return tokenCache.get(key);
    }
}
