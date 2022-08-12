package com.eatnow.search.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.eatnow.search.utils.FeignErrorDecoder;

import feign.codec.ErrorDecoder;

@Configuration
public class AppConfig {

    @Bean
    public ErrorDecoder errorDecoder() {
        return new FeignErrorDecoder();
    }
}
