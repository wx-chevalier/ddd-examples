package icu.ngte.realworld.api.config.db;

import icu.ngte.udma.core.infra.mybatis.config.AbstractMyBatisConfig;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@MapperScan(basePackages = "icu.ngte.realworld.infra")
public class MybatisConfig extends AbstractMyBatisConfig {

}
