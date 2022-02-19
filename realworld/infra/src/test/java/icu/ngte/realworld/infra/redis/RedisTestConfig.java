package icu.ngte.realworld.infra.redis;

import java.io.IOException;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.core.io.Resource;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import redis.embedded.RedisServer;

@TestConfiguration
@EnableTransactionManagement
public class RedisTestConfig extends AbstractRedissonConfig {

  @Value("classpath:/redisson.yaml")
  Resource configFile;

  private RedisServer redisServer;

  public RedisTestConfig() {
    this.redisServer = new RedisServer(6379);
  }

  @Override
  public Config getConfig() {
    try {
      return Config.fromYAML(configFile.getInputStream());
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }
  }

  @PostConstruct
  public void postConstruct() {
    redisServer.start();
  }

  @PreDestroy
  public void preDestroy() {
    redisServer.stop();
  }
}
