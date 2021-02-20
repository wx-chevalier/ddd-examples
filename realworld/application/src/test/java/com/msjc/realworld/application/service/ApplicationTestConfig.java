package com.msjc.realworld.application.service;

import com.udma.core.infra.mybatis.config.AbstractH2MyBatisDbConfig;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@MapperScan("com.msjc.realworld.infra.dmr")
@ComponentScan("com.msjc.realworld")
public class ApplicationTestConfig extends AbstractH2MyBatisDbConfig {

  @Override
  protected void configDataSourceBuilder(EmbeddedDatabaseBuilder builder) {}
}
