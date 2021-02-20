package com.msjc.realworld.infra.dmr.readservice;

import com.msjc.realworld.infra.dmr.data.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserReadService {

  UserData findByUsername(@Param("username") String username);

  UserData findById(@Param("id") Long id);
}
