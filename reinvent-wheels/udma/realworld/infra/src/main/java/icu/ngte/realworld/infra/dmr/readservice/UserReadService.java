package icu.ngte.realworld.infra.dmr.readservice;

import icu.ngte.realworld.infra.dmr.data.UserData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserReadService {

  UserData findByUsername(@Param("username") String username);

  UserData findById(@Param("id") Long id);
}
