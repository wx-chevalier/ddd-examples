package com.msjc.realworld.infra.dmr;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.util.Map.Entry;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {MybatisTestConfig.class})
public class DmrMappersTest {

  @Autowired ApplicationContext context;

  @SuppressWarnings("rawtypes")
  @Test
  void test_data_object_maps_to_db() {
    for (Entry<String, ServiceImpl> entry : context.getBeansOfType(ServiceImpl.class).entrySet()) {
      assertEquals(0, entry.getValue().count());
      assertEquals(0, entry.getValue().list().size());
    }
  }
}
