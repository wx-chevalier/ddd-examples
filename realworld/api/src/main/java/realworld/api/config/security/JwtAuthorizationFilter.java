package realworld.api.config.security;

import realworld.api.config.LogMdcKeys;
import com.msjc.analytics.common.data.SecurityUser;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Slf4j
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

  private static final String AUTHORIZATION = "Authorization";
  private static final String BEARER = "Bearer ";

  final JwtTokenService tokenService;

  public JwtAuthorizationFilter(
      AuthenticationManager authenticationManager, JwtTokenService tokenService) {
    super(authenticationManager);
    this.tokenService = tokenService;
  }

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain chain)
      throws IOException, ServletException {
    String authorization = request.getHeader("Authorization");

    if (authorization != null && authorization.startsWith(BEARER)) {
      verifyJwtToken(authorization.substring(BEARER.length()));
    }

    chain.doFilter(request, response);
  }

  private void verifyJwtToken(String token) {
    try {
      SecurityUser securityUser = tokenService.parseBearerToken(token);

      MDC.put(LogMdcKeys.USER_SUMMARY_ID, getSecurityUserMdcValue(securityUser));
      MDC.put(LogMdcKeys.USER_DETAIL_ID, getSecurityUserMdcValue(securityUser));

      UsernamePasswordAuthenticationToken authentication =
          new UsernamePasswordAuthenticationToken(
              securityUser,
              null,
              securityUser.getAuthorities().map(SimpleGrantedAuthority::new).toJavaList());

      SecurityContextHolder.getContext().setAuthentication(authentication);
    } catch (Exception e) {
      log.debug("illegal token", e);
    }
  }

  private String getSecurityUserMdcValue(SecurityUser securityUser) {
    return String.format("[%s]", securityUser.toString());
  }
}
