package realworld.api.config.security;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;
import static com.google.common.base.Preconditions.checkArgument;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.msjc.analytics.common.data.DataTools;
import com.msjc.analytics.common.data.EntityTypes;
import com.msjc.analytics.common.data.PersonId;
import com.msjc.analytics.common.data.SecurityUser;
import com.msjc.analytics.common.data.TenantId;
import com.msjc.analytics.common.data.UserId;
import com.msjc.domain.EntityId;
import io.vavr.API;
import io.vavr.collection.HashMap;
import io.vavr.collection.List;
import io.vavr.collection.Map;
import io.vavr.control.Option;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.function.Function;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenServiceImpl implements JwtTokenService {

  private static final String PERSON_ID = "personId";
  private static final String USER_ID = "userId";
  private static final String TENANT_ID = "tenantId";

  private static final String USERNAME = "username";
  private static final String NICKNAME = "nickname";
  private static final String SCOPES = "scopes";
  private static final String ROLES = "roles";

  final JwtTokenProperties tokenProperties;

  @Override
  public SecurityUser parseBearerToken(String token) {
    DecodedJWT jwt;
    try {
      jwt =
          JWT.require(HMAC512(tokenProperties.getSecret().getBytes(StandardCharsets.UTF_8)))
              .build()
              .verify(token);
    } catch (TokenExpiredException e) {
      log.info("JWT Token is expired: {} {}", token, e.getMessage());
      throw new CredentialsExpiredException("Expired jwt token", e);
    } catch (JWTVerificationException e) {
      log.error("Invalid JWT token: {} {}", token, e.getMessage());
      throw new BadCredentialsException("Invalid jwt token: " + token, e);
    }

    Map<String, Claim> claims = HashMap.ofAll(jwt.getClaims());

    String username = claims.get(USERNAME).map(Claim::asString).getOrNull();
    String nickname = claims.get(NICKNAME).map(Claim::asString).getOrNull();
    Option<TenantId> tenantId =
        toEntityId(claims.get(TENANT_ID), TenantId.class, EntityTypes.TENANT);
    Option<PersonId> personId =
        toEntityId(claims.get(PERSON_ID), PersonId.class, EntityTypes.PERSON);
    Option<UserId> userId = toEntityId(claims.get(USER_ID), UserId.class, EntityTypes.USER);
    List<String> authorities = getAuthorities(claims);

    return new SecurityUserImpl(username, nickname, tenantId, personId, userId, authorities);
  }

  private List<String> getAuthorities(Map<String, Claim> claims) {
    List<String> scopes =
        claims.get(SCOPES).map(v -> v.asList(String.class)).map(List::ofAll).getOrElse(API::List);
    checkArgument(scopes != null && !scopes.isEmpty(), "no scope found");

    List<String> roles =
        claims
            .get(ROLES)
            .map(v -> v.asList(String.class))
            .map(List::ofAll)
            .getOrElse(API::List)
            .map(v -> v.startsWith("ROLE_") ? v : "ROLE_" + v);

    return scopes.appendAll(roles);
  }

  @SuppressWarnings("unchecked")
  private <T extends EntityId> Option<T> toEntityId(
      Option<Claim> claim, Class<T> clazz, String entityType) {
    Option<EntityId> tmp =
        claim
            .map(Claim::asString)
            .map(
                v -> {
                  Long longVal;
                  if (v.startsWith(entityType + "-")) {
                    longVal = Long.parseLong(v.substring(entityType.length() + 1));
                  } else {
                    longVal = API.Try(() -> Long.parseLong(v)).getOrNull();
                  }

                  if (longVal != null) {
                    Function<Long, EntityId> c = DataTools.createLongConstructorForEntityId(clazz);
                    return c.apply(longVal);
                  } else {
                    Function<UUID, EntityId> c = DataTools.getUUIDConstructor(clazz);
                    return c.apply(UUID.fromString(v));
                  }
                });

    return tmp.map(v -> (T) v);
  }
}
