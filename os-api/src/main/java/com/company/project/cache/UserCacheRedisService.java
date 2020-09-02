package com.company.project.cache;

import com.company.project.modules.sys.entity.User;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Service;

@Service
public class UserCacheRedisService implements UserCacheService {

    private final RedisTemplate<String, String> redisTemplate;

    public UserCacheRedisService(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }


    @Override
    public void putUser(String token, User user) {
        redisTemplate.setHashValueSerializer(new Jackson2JsonRedisSerializer<>(User.class));
        HashOperations<String, Object, User> stringObjectObjectHashOperations = redisTemplate.opsForHash();
        stringObjectObjectHashOperations.put("user", token, user);
    }

    @Override
    public User getUser(String token) {
        redisTemplate.setHashValueSerializer(new Jackson2JsonRedisSerializer<>(User.class));
        HashOperations<String, Object, User> stringObjectObjectHashOperations = redisTemplate.opsForHash();
        return stringObjectObjectHashOperations.get("user", token);
    }

    @Override
    public void putToken(String key, String token) {
        redisTemplate.setHashValueSerializer(new StringRedisSerializer());
        HashOperations<String, String, String> stringObjectObjectHashOperations = redisTemplate.opsForHash();
        stringObjectObjectHashOperations.put("token", key, token);
    }

    @Override
    public String getToken(String key) {
        redisTemplate.setHashValueSerializer(new StringRedisSerializer());
        HashOperations<String, String, String> stringObjectObjectHashOperations = redisTemplate.opsForHash();
        return stringObjectObjectHashOperations.get("token", key);
    }
}
