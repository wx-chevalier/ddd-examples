package com.udma.core.infra.mybatis.config;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.baomidou.mybatisplus.core.config.GlobalConfig.DbConfig;
import com.baomidou.mybatisplus.extension.incrementer.H2KeyGenerator;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.google.common.base.Strings;
import com.udma.core.infra.mybatis.handler.MyBatisHelper;
import io.vavr.collection.List;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import javax.sql.DataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

public abstract class AbstractH2MyBatisDbConfig extends AbstractMyBatisConfig {

  protected abstract void configDataSourceBuilder(EmbeddedDatabaseBuilder builder);

  @Bean
  public DataSource dataSource() throws IOException {

    try (BufferedReader reader =
        new BufferedReader(
            new InputStreamReader(
                getClass().getResourceAsStream("/db/migration-scripts.txt"), Charset.defaultCharset()))) {
      String[] scripts =
          List.ofAll(reader.lines()).filterNot(Strings::isNullOrEmpty).toJavaArray(String[]::new);
      EmbeddedDatabaseBuilder builder =
          new EmbeddedDatabaseBuilder()
              .setName(getClass().getTypeName() + Math.random())
              .setType(EmbeddedDatabaseType.H2)
              .addScripts(scripts);

      configDataSourceBuilder(builder);

      return builder.build();
    }
  }

  @Bean
  public DataSourceTransactionManager transactionManager(DataSource dataSource) {
    return new DataSourceTransactionManager(dataSource);
  }

  @Bean
  public GlobalConfig globalConfig() {
    return new GlobalConfig()
        .setEnableSqlRunner(true)
        .setDbConfig(
            new DbConfig()
                .setLogicDeleteValue("1")
                .setLogicNotDeleteValue("0")
                .setKeyGenerator(new H2KeyGenerator())
                .setIdType(IdType.AUTO));
  }

  @Bean("mybatisSqlSession")
  public SqlSessionFactory sqlSessionFactory(
      DataSource dataSource, GlobalConfig globalConfig, PaginationInterceptor paginationInterceptor)
      throws Exception {
    return MyBatisHelper.createMyBatisSqlSessionFactory(
        dataSource, globalConfig, paginationInterceptor);
  }
}
