package com.msjc.realworld.infra.redis;

import java.io.IOException;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
public class RedisTestConfig extends AbstractRedissonConfig {
  @Value("classpath:/redisson.yaml")
  Resource configFile;

  @Override
  public Config getConfig() {
    try {
      return Config.fromYAML(configFile.getInputStream());
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }
  }
}
