package com.eatnow.cart.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.eatnow.cart.utils.RedisCache;

@Configuration
public class AppConfig {

    @Value("${cart.redis.host}")
    private String redisHost;

    @Value("${cart.redis.port}")
    private int redisPort;

    @Bean
	public RedisCache getRedisCache() {

		return new RedisCache(redisHost, redisPort);
	} 
}
