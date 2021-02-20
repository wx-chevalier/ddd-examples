package realworld.api.config.security;

import com.msjc.analytics.common.data.SecurityUser;

public interface JwtTokenService {

  SecurityUser parseBearerToken(String token);
}
