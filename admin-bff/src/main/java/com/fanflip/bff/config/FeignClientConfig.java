package com.fanflip.bff.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignClientConfig {
    @Bean
    public FeignClientInterceptor jwtRequestInterceptor() {
        return new FeignClientInterceptor();
    }
}
