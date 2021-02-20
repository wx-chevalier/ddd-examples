package com.msjc.analytics.common.spring;

import com.msjc.analytics.common.data.SecurityUser;
import io.vavr.API;
import io.vavr.control.Option;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityContextHelper {

  public static Option<SecurityUser> getSecurityUser() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null) {
      return API.None();
    }
    Object principal = authentication.getPrincipal();
    if (principal instanceof SecurityUser) {
      return API.Option((SecurityUser) principal);
    } else {
      return API.None();
    }
  }
}
