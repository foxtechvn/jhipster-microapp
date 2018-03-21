package com.ft.config.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class AppCacheConfiguration {

	@Autowired
	CacheManager cacheManager;

	@Autowired
	Environment env;

	@Bean
	public Cache cache() {
		return cacheManager.getCache(env.getProperty("spring.application.name"));
	}
}
