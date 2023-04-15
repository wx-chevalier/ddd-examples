package icu.ngte.realworld.infra.dmr;

import icu.ngte.udma.core.infra.mybatis.config.AbstractHsqlMyBatisDbConfig;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@MapperScan("icu.ngte.realworld.infra.dmr")
@ComponentScan("icu.ngte.realworld.infra")
public class MybatisTestConfig extends AbstractHsqlMyBatisDbConfig {

  @Override
  protected void configDataSourceBuilder(EmbeddedDatabaseBuilder builder) {

  }
}
