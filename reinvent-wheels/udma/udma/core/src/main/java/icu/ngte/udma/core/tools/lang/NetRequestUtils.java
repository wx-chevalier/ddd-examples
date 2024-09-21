package icu.ngte.udma.core.tools.lang;

import icu.ngte.udma.core.tools.ds.HashUtils;
import icu.ngte.udma.core.tools.ds.JsonTools;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Map.Entry;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Slf4j
@Component
public class NetRequestUtils {

  private static final CloseableHttpClient httpClient;

  private static final RequestConfig requestConfig =
      RequestConfig.custom()
          .setSocketTimeout(30000)
          .setConnectTimeout(5000)
          .setConnectionRequestTimeout(5000)
          .build();

  static {
    PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager();
    connManager.setMaxTotal(300);
    connManager.setDefaultMaxPerRoute(300);
    httpClient = HttpClients.custom().setConnectionManager(connManager).build();
  }

  /** 发送Get请求，并且自动反序列化(默认不添加请求头参数) */
  public static <T> T get(String url, String param, Class<T> clazz) {
    return get(url, param, null, clazz);
  }

  /** 发送Get请求，并且自动反序列化 */
  public static <T> T get(
      String url, String param, @Nullable Map<String, String> headerParam, Class<T> clazz) {
    String responseBody = get(url, param, headerParam);
    return JsonTools.fromString(responseBody, clazz);
  }

  // 发送 Post 请求
  public static String post(String url, Serializable requestBodyObj) {
    String requestBody = JsonTools.toString(requestBodyObj);
    return post(url, requestBody);
  }

  // 发送 Post 请求
  public static <T> T post(String url, Serializable requestBodyObj, Class<T> clazz) {
    String requestBody = JsonTools.toString(requestBodyObj);
    String responseBody = post(url, requestBody);
    if (clazz == null) {
      return null;
    }

    return JsonTools.fromString(responseBody, clazz);
  }

  /** 发送Post 请求 */
  private static String post(String url, String requestBody) {
    log.info("发送 post 请求: url = {} requestBody={} ", url, requestBody);
    HttpPost postClient = new HttpPost(url);

    HttpEntity entity = new ByteArrayEntity(requestBody.getBytes(Charset.defaultCharset()));
    postClient.setEntity(entity);
    postClient.setConfig(requestConfig);

    return send(postClient);
  }

  /** 发送Get请求 */
  private static String get(String url, String param, Map<String, String> headerParam) {
    String path = url + "?" + param;
    String requestId = HashUtils.md5(path);
    log.info("发送Get请求: requestId = {} url={} params={}", requestId, url, param);

    HttpGet httpget = new HttpGet(path);
    httpget.setConfig(requestConfig);

    // 添加请求头
    if (!CollectionUtils.isEmpty(headerParam)) {
      for (Entry<String, String> entry : headerParam.entrySet()) {
        String key = entry.getKey();
        String value = entry.getValue();

        log.debug("API 请求添加参数[{},{}]", key, value);
        httpget.addHeader(key, value);
      }
    }
    return send(httpget);
  }

  private static String send(HttpUriRequest request) {
    try (CloseableHttpResponse response = httpClient.execute(request)) {
      if (response == null) {
        return null;
      }
      String resultStr = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
      log.info("请求响应结果:{}", resultStr);
      return resultStr;
    } catch (Throwable e) {
      log.error("发送请求出错: HttpMethod: {} uri={}", request.getMethod(), request.getURI());
      throw new RuntimeException("发送网络请求出现异常", e);
    }
  }
}
