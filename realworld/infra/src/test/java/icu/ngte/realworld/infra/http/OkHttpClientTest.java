package icu.ngte.realworld.infra.http;

import okhttp3.OkHttpClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {HttpTestConfig.class})
public class OkHttpClientTest {

  @Autowired OkHttpClient okHttpClient;

  @Test
  public void testGet() {
    System.out.println(okHttpClient);
  }
}
