package icu.ngte.realworld.api.config.properties;

import lombok.Data;

@Data
public class JwtTokenConfig {
  private String secret;

  private String issuer = "ngte";

  private Integer expirationSec = 36000;
}
