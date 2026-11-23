package com.readmate.buddle.global.config;

import static org.springframework.data.redis.core.RedisKeyValueAdapter.EnableKeyspaceEvents.ON_STARTUP;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

@Configuration
@EnableRedisRepositories(
    basePackages = "com.readmate.buddle.domain",
    enableKeyspaceEvents = ON_STARTUP)
public class RedisConfig {}
