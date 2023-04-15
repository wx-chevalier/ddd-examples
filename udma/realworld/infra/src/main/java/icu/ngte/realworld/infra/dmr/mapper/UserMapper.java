package icu.ngte.realworld.infra.dmr.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import icu.ngte.realworld.domain.user.FollowRelationVO;
import icu.ngte.realworld.domain.user.UserDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper extends BaseMapper<UserDO> {

  UserDO findById(@Param("id") String id);

  UserDO findByUsername(@Param("username") String username);

  UserDO findByEmail(@Param("email") String email);

  FollowRelationVO findRelation(@Param("userId") Long userId, @Param("targetId") Long targetId);

  void saveRelation(@Param("followRelation") FollowRelationVO followRelation);

  void deleteRelation(@Param("followRelation") FollowRelationVO followRelation);
}
