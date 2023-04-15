package icu.ngte.realworld.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import icu.ngte.realworld.api.config.properties.*;
import io.vavr.jackson.datatype.VavrModule;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RedissonClient;
import org.redisson.spring.cache.RedissonSpringCacheManager;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Slf4j
@EnableAsync(proxyTargetClass = true)
@EnableCaching
@EnableScheduling
@SpringBootApplication
public class RealworldApplication implements AsyncConfigurer {

  private final ApplicationProperties applicationProperties;
  private final ObjectMapper objectMapper;

  public RealworldApplication(ApplicationProperties applicationProperties, ObjectMapper objectMapper) {
    this.applicationProperties = applicationProperties;
    this.objectMapper = objectMapper;
  }

  public static void main(String[] args) {
    new SpringApplicationBuilder(RealworldApplication.class).run(args);
  }

  @PostConstruct
  public void init() {
    objectMapper.registerModule(new VavrModule());
  }

  @Bean
  public JwtTokenConfig jwtTokenConfig() {
    return applicationProperties.getSecurity().getJwt();
  }

  @Primary
  @Bean
  @ConditionalOnBean(RedissonClient.class)
  public CacheManager cacheManager(RedissonClient client) {
    return new RedissonSpringCacheManager(client);
  }

  @Override
  public Executor getAsyncExecutor() {
    ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
    executor.setCorePoolSize(20);
    executor.setMaxPoolSize(50);
    executor.setQueueCapacity(100);
    executor.setThreadNamePrefix("msjc-web-");
    executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
    executor.initialize();
    return executor;
  }
}
