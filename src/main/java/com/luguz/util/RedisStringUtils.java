package com.luguz.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author Guz
 * @create 2022-07--31 22:24
 */
@Component
@Slf4j //使用了lombok的标签 方便写日志
public class RedisStringUtils {

    @Resource(name = "redisTemplate1")
    private RedisTemplate<String, String> redisTemplate;

    public Object getObject(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public void saveWithExpireTime(String key, String object, long timeout) {
        redisTemplate.opsForValue().set(key, object, timeout, TimeUnit.SECONDS);
    }

    /**
     * List 数据结构
     */
    public List<String> range(String key) {
        try {
            return redisTemplate.opsForList().range(key, 0, -1);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * hash 数据结构
     * 如果 field 存在就不在修改
     */
    public void hsetIfAbsent(String key, String field, String value) {
        try {
            redisTemplate.opsForHash().putIfAbsent(key, field, value);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    public boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }

    /**
     * 将一个元素及其 score 值加入到有序集 key 当中
     * O(M*log(N))， N 是有序集的基数， M 为成功添加的新成员的数量
     *
     * @param key    key
     * @param value  member
     * @param source score
     * @return 是否成功
     */
    public Boolean zAdd(String key, String value, double source) {
        try {
            return redisTemplate.opsForZSet().add(key, value, source);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }
}