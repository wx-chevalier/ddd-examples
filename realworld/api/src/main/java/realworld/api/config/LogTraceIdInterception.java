package realworld.api.config;

import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Nullable;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

@Slf4j
@Component
public class LogTraceIdInterception extends HandlerInterceptorAdapter {

  private static final String REQUEST_ID_HEADER = "x-request-id";

  @Override
  public boolean preHandle(
      HttpServletRequest request, HttpServletResponse response, Object handler) {
    String requestId = validateRequestId(request.getHeader(REQUEST_ID_HEADER));
    MDC.put(LogMdcKeys.TRACE_ID, requestId);
    response.addHeader(REQUEST_ID_HEADER, requestId);
    return true;
  }

  @Override
  public void afterCompletion(
      HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
    MDC.clear();
  }

  @Nullable
  private String validateRequestId(String requestId) {
    if (StringUtils.isEmpty(requestId)) {
      return UUID.randomUUID().toString().replaceAll("-", "");
    }
    if (requestId.matches("[a-zA-Z0-9]+")) {
      return requestId;
    } else {
      return null;
    }
  }
}
