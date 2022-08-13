package com.luguz.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Guz
 * @create 2022-07--31 22:21
 */

@Component
@ConfigurationProperties(prefix = "spring.redis.cluster")
@Data //使用了lombok的标签 如果未引用lombok需写getter 和 setter方法
public class RedisClusterConfigProperties {
    private List<String> nodes;
    private Integer maxAttempts;
    private Integer connectionTimeout;
    private Integer soTimeout;
    private String password;
}
