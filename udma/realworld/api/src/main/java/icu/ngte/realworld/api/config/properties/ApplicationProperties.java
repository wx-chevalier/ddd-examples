package icu.ngte.realworld.api.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "application")
@Data
public class ApplicationProperties {
  private SecurityProperties security;
}
