package com.msjc.realworld.domain.user;

import com.baomidou.mybatisplus.annotation.TableName;
import com.udma.core.infra.mybatis.basedo.BaseEntity;
import java.util.UUID;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true, of = {"id"})
@TableName("users")
public class UserDO extends BaseEntity<UserDO> {

  private String email;
  private String username;
  private String password;
  private String bio;
  private String image;

  public UserDO(String email, String username, String password, String bio, String image) {
    this.uuid = UUID.randomUUID().toString();
    this.email = email;
    this.username = username;
    this.password = password;
    this.bio = bio;
    this.image = image;
  }

  public void update(String email, String username, String password, String bio, String image) {
    if (!"".equals(email)) {
      this.email = email;
    }

    if (!"".equals(username)) {
      this.username = username;
    }

    if (!"".equals(password)) {
      this.password = password;
    }

    if (!"".equals(bio)) {
      this.bio = bio;
    }

    if (!"".equals(image)) {
      this.image = image;
    }
  }
}
