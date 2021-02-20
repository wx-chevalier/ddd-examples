package com.msjc.realworld.infra.redis;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.redisson.api.RAtomicLong;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {RedisTestConfig.class})
public class RedissonTest {
  @Autowired RedissonClient redissonClient;

  @Test
  public void testKeys() {

    String key = "test:keyNotDefined";

    redissonClient.getKeys().delete(key);

    RAtomicLong rAtomicLong = redissonClient.getAtomicLong(key);
    assertEquals(rAtomicLong.get(), 0);

    rAtomicLong.incrementAndGet();

    RAtomicLong rAtomicLong2 = redissonClient.getAtomicLong(key);
    assertEquals(rAtomicLong2.get(), 1);
  }

  @Test
  public void testStr() {
    redissonClient.getKeys().delete("test:bucket");

    RBucket<String> rBucket = redissonClient.getBucket("test:bucket");
    assertEquals(rBucket.size(), 0);
    rBucket.set("testtest");

    RBucket<String> rBucket2 = redissonClient.getBucket("test:bucket");
    assertEquals(rBucket2.size(), 10);
    assertEquals(rBucket2.get(), "testtest");
  }
}
