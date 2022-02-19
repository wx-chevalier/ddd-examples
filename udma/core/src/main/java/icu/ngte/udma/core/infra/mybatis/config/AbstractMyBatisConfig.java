package icu.ngte.udma.core.infra.mybatis.config;

import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.vavr.API;
import io.vavr.collection.List;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.springframework.context.annotation.Bean;

public abstract class AbstractMyBatisConfig {

  protected List<String> getExtraScripts() {
    return API.List();
  }

  /**
   * 分页插件
   */
  @Bean
  public PaginationInterceptor paginationInterceptor() {
    PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
    paginationInterceptor.setLimit(100);
    return paginationInterceptor;
  }

  @Bean
  public MybatisPlusInterceptor mybatisPlusInterceptor() {
    MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
    PaginationInnerInterceptor paginationInnerInterceptor = new PaginationInnerInterceptor();
    paginationInnerInterceptor.setMaxLimit(100L);
    interceptor.addInnerInterceptor(paginationInnerInterceptor);
    interceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());
    return interceptor;
  }

  @Bean
  public TypeHandlerRegistry typeHandlers(SqlSessionFactory sqlSessionFactory) {
    TypeHandlerRegistry typeHandlerRegistry =
        sqlSessionFactory.getConfiguration().getTypeHandlerRegistry();

    typeHandlerRegistry.register(JsonNode.class, JacksonTypeHandler.class);
    typeHandlerRegistry.register(ObjectNode.class, JacksonTypeHandler.class);
    typeHandlerRegistry.register(ArrayNode.class, JacksonTypeHandler.class);

    return typeHandlerRegistry;
  }

}