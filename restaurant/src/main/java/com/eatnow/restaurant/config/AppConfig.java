package com.eatnow.restaurant.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.eatnow.restaurant.utils.RedisCache;

@Configuration
public class AppConfig {

    @Value("${restaurant.redis.host}")
    private String redisHost;

    @Value("${restaurant.redis.port}")
    private int redisPort;

    @Bean
	public RedisCache getRedisCache() {

		return new RedisCache(redisHost, redisPort);
	} 
}
