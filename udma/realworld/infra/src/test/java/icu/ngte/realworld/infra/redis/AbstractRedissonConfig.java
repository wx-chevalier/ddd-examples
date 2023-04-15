package icu.ngte.realworld.infra.redis;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.spring.data.connection.RedissonConnectionFactory;
import org.springframework.context.annotation.Bean;

public abstract class AbstractRedissonConfig {
  public abstract Config getConfig();

  @Bean
  public RedissonConnectionFactory redissonConnectionFactory(RedissonClient redisson) {
    return new RedissonConnectionFactory(redisson);
  }

  @Bean(destroyMethod = "shutdown")
  public RedissonClient redissonClient() {
    Config config = this.getConfig();
    return Redisson.create(config);
  }
}
