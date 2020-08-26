package com.company.project.modules.sys.component;

import cn.hutool.cache.CacheUtil;
import cn.hutool.cache.impl.TimedCache;
import cn.hutool.core.date.DateUnit;
import com.company.project.modules.sys.entity.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * 用户信息缓存
 * 定时缓存，对被缓存的对象定义一个过期时间，当对象超过过期时间会被清理。此缓存没有容量限制，对象只有在过期后才会被移除。
 */
@Slf4j
@Component
public class UserTimedCache {

    private TimedCache<String, String> timedCache;

    private ObjectMapper objectMapper;

    public UserTimedCache(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        //创建缓存，默认一小时过期
        timedCache = CacheUtil.newTimedCache(DateUnit.HOUR.getMillis());
    }

    public synchronized void put(String key, String userInfo) {
        timedCache.put(key, userInfo);
    }

    public synchronized User get(String key) {
        String userInfo = timedCache.get(key);
        if (StringUtils.isEmpty(userInfo)) {
            return null;
        }
        try {
            return objectMapper.readValue(userInfo, User.class);
        } catch (JsonProcessingException e) {
            log.info(e.getMessage());
            return null;
        }
    }
}
