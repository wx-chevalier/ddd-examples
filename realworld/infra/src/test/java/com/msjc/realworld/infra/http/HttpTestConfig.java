package com.msjc.realworld.infra.http;

import io.freefair.spring.okhttp.OkHttpProperties;
import java.time.Duration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = {"io.freefair.spring.okhttp"})
public class HttpTestConfig {

  @Bean
  OkHttpProperties okHttpProperties() {
    OkHttpProperties okHttpProperties = new OkHttpProperties();
    okHttpProperties.setReadTimeout(Duration.ofSeconds(5 * 60));

    return okHttpProperties;
  }
}
