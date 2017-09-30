package com.onecoderspace.base.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

/**
 * spring session配置
 * @author yangwk
 * @version v1.0
 * @time 2017年9月30日 下午3:32:40
 */
@Configuration
@EnableRedisHttpSession(maxInactiveIntervalInSeconds=7200,redisNamespace="base")
public class RedisSessionConfig {

}
