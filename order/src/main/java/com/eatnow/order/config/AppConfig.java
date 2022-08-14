package com.eatnow.order.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.eatnow.order.utils.FeignErrorDecoder;
import com.eatnow.order.utils.RedisCache;

import feign.codec.ErrorDecoder;

@Configuration
public class AppConfig {

    @Value("${order.redis.host}")
    private String redisHost;

    @Value("${order.redis.port}")
    private int redisPort;

    @Bean
	public RedisCache getRedisCache() {

		return new RedisCache(redisHost, redisPort);
	}

    @Bean
    public ErrorDecoder errorDecoder() {
        return new FeignErrorDecoder();
    }

}
