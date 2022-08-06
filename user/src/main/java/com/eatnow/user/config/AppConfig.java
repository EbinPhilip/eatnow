package com.eatnow.user.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.eatnow.user.utils.RedisCache;

@Configuration
public class AppConfig {

    @Value("${user.redis.host}")
    private String redisHost;

    @Value("${user.redis.port}")
    private int redisPort;

    @Bean
	public RedisCache getRedisCache() {

		return new RedisCache(redisHost, redisPort);
	} 
}
