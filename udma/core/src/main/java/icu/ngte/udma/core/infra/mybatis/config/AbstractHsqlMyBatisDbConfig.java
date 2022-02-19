package icu.ngte.udma.core.infra.mybatis.config;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.baomidou.mybatisplus.core.config.GlobalConfig.DbConfig;
import com.baomidou.mybatisplus.extension.incrementer.H2KeyGenerator;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.google.common.base.Strings;
import icu.ngte.udma.core.infra.mybatis.handler.MyBatisHelper;
import io.vavr.collection.List;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.sql.Driver;
import javax.sql.DataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.jdbc.datasource.embedded.ConnectionProperties;
import org.springframework.jdbc.datasource.embedded.DataSourceFactory;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

public abstract class AbstractHsqlMyBatisDbConfig extends AbstractMyBatisConfig {

  protected abstract void configDataSourceBuilder(EmbeddedDatabaseBuilder builder);

  @Bean
  public DataSource dataSource() throws IOException {

    try (BufferedReader reader =
        new BufferedReader(
            new InputStreamReader(
                getClass().getResourceAsStream("/db/migration-scripts.txt"),
                Charset.defaultCharset()))) {

      String[] scripts =
          List.ofAll(reader.lines())
              .appendAll(getExtraScripts())
              .filterNot(Strings::isNullOrEmpty)
              .toJavaArray(String[]::new);

      EmbeddedDatabaseBuilder builder =
          this.getEmbeddedDatabaseBuilder()
              .setName(getClass().getTypeName() + Math.random())
              .setType(EmbeddedDatabaseType.HSQL)
              .addScripts(scripts);

      configDataSourceBuilder(builder);

      return builder.build();

    }
  }

  private EmbeddedDatabaseBuilder getEmbeddedDatabaseBuilder() {
    EmbeddedDatabaseBuilder databaseBuilder = new EmbeddedDatabaseBuilder();
    databaseBuilder.setDataSourceFactory(
        new DataSourceFactory() {
          private final SimpleDriverDataSource dataSource = new SimpleDriverDataSource();

          @Override
          public ConnectionProperties getConnectionProperties() {
            return new ConnectionProperties() {
              @Override
              public void setDriverClass(Class<? extends Driver> driverClass) {
                dataSource.setDriverClass(org.hsqldb.jdbcDriver.class);
              }

              @Override
              public void setUrl(String url) {
                dataSource.setUrl("jdbc:hsqldb:mem:testdb;sql.syntax_mys=true;sql.syntax_ora=true");
              }

              @Override
              public void setUsername(String username) {
                dataSource.setUsername("sa");
              }

              @Override
              public void setPassword(String password) {
                dataSource.setPassword("");
              }
            };
          }

          @Override
          public DataSource getDataSource() {
            return dataSource;
          }
        });

    return databaseBuilder;
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
