package icu.ngte.realworld.api.config.log;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.swagger.annotations.ApiModel;
import java.util.Collections;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

  @Bean
  public Docket docket() {
    return createSwagger2Docket()
        .apiInfo(apiInfo())
        .select()
        .apis(RequestHandlerSelectors.basePackage("icu.ngte.cloud.analytics"))
        .paths(PathSelectors.any())
        .build()
        .securityContexts(Collections.singletonList(securityContext()))
        .securitySchemes(Collections.singletonList(apiKey()));
  }

  private Docket createSwagger2Docket() {
    Docket docket = new Docket(DocumentationType.SWAGGER_2);
    docket.directModelSubstitute(ObjectNode.class, JsonObject.class);
    docket.directModelSubstitute(ArrayNode.class, JsonArray.class);
    docket.directModelSubstitute(JsonNode.class, Json.class);
    return docket;
  }

  private ApiInfo apiInfo() {
    return new ApiInfoBuilder().title("msjc Analytics").version("0.1").build();
  }

  private SecurityContext securityContext() {
    return SecurityContext.builder()
        .securityReferences(defaultAuth())
        .forPaths(PathSelectors.any())
        .build();
  }

  private List<SecurityReference> defaultAuth() {
    AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
    AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
    authorizationScopes[0] = authorizationScope;
    return Collections.singletonList(new SecurityReference("Authorization", authorizationScopes));
  }

  private ApiKey apiKey() {
    return new ApiKey("Authorization", "Authorization", "header");
  }

  @ApiModel("JSON 对象")
  public static class JsonObject {}

  @ApiModel("JSON 数组")
  public static class JsonArray {}

  @ApiModel("JSON")
  public static class Json {}
}
