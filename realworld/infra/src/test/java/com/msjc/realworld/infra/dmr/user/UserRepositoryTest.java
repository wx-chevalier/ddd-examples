package com.msjc.realworld.infra.dmr.user;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import cn.hutool.core.util.RandomUtil;
import com.msjc.realworld.domain.user.FollowRelationVO;
import com.msjc.realworld.domain.user.UserDO;
import com.msjc.realworld.domain.user.UserRepository;
import com.msjc.realworld.infra.dmr.MybatisTestConfig;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(
    classes = {MybatisTestConfig.class},
    properties = {
        "logging.level.com.msjc.realworld.infra.dmr=debug",
        "mybatis-plus.mapper-locations: classpath:/mapper/*Mapper.xml"
    })
public class UserRepositoryTest {

  UserDO user;

  @Autowired
  private UserRepository userRepository;

  @BeforeEach
  private void setUp() {
    user = new UserDO(RandomUtil.randomString(5) + "@qq.com", RandomUtil.randomString(5), "123", "", "default");
    userRepository.saveUser(user);
  }

  @Test
  public void test_should_save_and_fetch_user_success() {

    UserDO user = new UserDO("test@test.com", "test-name", "123", "", "default");
    userRepository.saveUser(user);

    Optional<UserDO> userOptional = userRepository.findByUsername("test-name");
    assertEquals(userOptional.get(), user);
    Optional<UserDO> userOptional2 = userRepository.findByEmail("test@test.com");
    assertEquals(userOptional2.get(), user);
  }

  @Test
  public void test_should_update_user_success() {

    String newEmail = "newemail@email.com";
    user.update(newEmail, "", "", "", "");
    userRepository.saveUser(user);
    Optional<UserDO> optional = userRepository.findByUsername(user.getUsername());
    assertTrue(optional.isPresent());
    assertEquals(optional.get().getEmail(), newEmail);

    String newUsername = "newUsername";
    user.update("", newUsername, "", "", "");
    userRepository.saveUser(user);
    optional = userRepository.findByEmail(user.getEmail());
    assertTrue(optional.isPresent());
    assertEquals(optional.get().getUsername(), newUsername);
    assertEquals(optional.get().getImage(), user.getImage());
  }

  @Test
  public void should_create_new_user_follow_success() {

    UserDO other = new UserDO("other1@example.com", "other1", "123", "", "");
    userRepository.saveUser(other);

    FollowRelationVO followRelation = new FollowRelationVO(user.getId(), other.getId());
    userRepository.saveRelation(followRelation);
    assertTrue(userRepository.findRelation(user.getId(), other.getId()).isPresent());
  }

  @Test
  public void should_unfollow_user_success() {

    UserDO other = new UserDO("other2@example.com", "other2", "123", "", "");
    userRepository.saveUser(other);

    FollowRelationVO followRelation = new FollowRelationVO(user.getId(), other.getId());
    userRepository.saveRelation(followRelation);

    userRepository.removeRelation(followRelation);
    assertFalse(userRepository.findRelation(user.getId(), other.getId()).isPresent());
  }
}
