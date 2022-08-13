package com.luguz.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPoolConfig;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.Set;
/**
 * @author Guz
 * @create 2022-07--31 22:22
 */
@Configuration
public class RedisConfig extends CachingConfigurerSupport {

    @Resource
    private RedisClusterConfigProperties clusterProperties;

    @Bean
    public RedisClusterConfiguration getClusterConfig() {
        RedisClusterConfiguration rcc = new RedisClusterConfiguration(clusterProperties.getNodes());
        rcc.setMaxRedirects(clusterProperties.getMaxAttempts());
        rcc.setPassword(RedisPassword.of(clusterProperties.getPassword()));
        return rcc;
    }

    @Bean
    public JedisCluster getJedisCluster() {
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        // 截取集群节点
        String[] cluster = clusterProperties.getNodes().toArray(new String[0]);
        // 创建set集合
        Set<HostAndPort> nodes = new HashSet<HostAndPort>();
        // 循环数组把集群节点添加到set集合中
        for (String node : cluster) {
            String[] host = node.split(":");
            //添加集群节点
            nodes.add(new HostAndPort(host[0], Integer.parseInt(host[1])));
        }
        return new JedisCluster(nodes, clusterProperties.getConnectionTimeout(), clusterProperties.getSoTimeout(), clusterProperties.getMaxAttempts(), clusterProperties.getPassword(), poolConfig);
    }


    @Bean
    public JedisConnectionFactory redisConnectionFactory(RedisClusterConfiguration cluster) {
        return new JedisConnectionFactory(cluster);
    }

    /**
     * RedisTemplate配置
     * key 和 value 都为String类型
     * 都使用Jackson2JsonRedisSerializer进行序列化
     */
    @Bean(name = "redisTemplate1")
    public RedisTemplate<String, String> redisTemplate(RedisConnectionFactory factory) {
        StringRedisTemplate template = new StringRedisTemplate(factory);
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);
        template.setValueSerializer(jackson2JsonRedisSerializer);
        template.afterPropertiesSet();
        return template;
    }

    /**
     * RedisTemplate配置
     * key 为String类型
     * value 为 Object 类型
     * 都使用Jackson2JsonRedisSerializer进行序列化
     */

    @Bean(name = "redisTemplate2")
    public RedisTemplate<String, Object> redisTemplate2(RedisConnectionFactory factory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        RedisSerializer<String> stringSerializer = new StringRedisSerializer();
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
        ObjectMapper om = new ObjectMapper();
        template.setConnectionFactory(factory);
        template.afterPropertiesSet();
        template.setKeySerializer(stringSerializer);
        template.setHashKeySerializer(stringSerializer);
        template.setHashValueSerializer(stringSerializer);
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);
        template.setValueSerializer(jackson2JsonRedisSerializer);
        template.afterPropertiesSet();
        return template;
    }
}