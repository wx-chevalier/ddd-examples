package com.msjc.realworld.infra.dmr.readservice;

import java.util.List;
import java.util.Set;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserRelationshipReadService {
  boolean isUserFollowing(
      @Param("userId") Long userId, @Param("anotherUserId") Long anotherUserId);

  Set<String> followingAuthors(@Param("userId") Long userId, @Param("ids") List<Long> ids);

  List<String> followedUsers(@Param("userId") Long userId);
}
