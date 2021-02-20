package realworld.api.config;

import com.udma.core.infra.mybatis.config.AbstractMyBatisConfig;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@MapperScan(basePackages = "com.msjc.realworld.infra")
public class MybatisConfig extends AbstractMyBatisConfig {

}
