package com.msjc.realworld.domain.user;

import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository {

  boolean saveUser(UserDO user);

  Optional<UserDO> findById(String id);

  Optional<UserDO> findByUsername(String username);

  Optional<UserDO> findByEmail(String email);

  void saveRelation(FollowRelationVO followRelation);

  Optional<FollowRelationVO> findRelation(Long userId, Long targetId);

  void removeRelation(FollowRelationVO followRelation);
}
