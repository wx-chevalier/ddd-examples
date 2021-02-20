package com.msjc.realworld.application.service;

import com.msjc.realworld.infra.dmr.data.UserData;
import com.msjc.realworld.infra.dmr.readservice.UserReadService;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class UserQueryService {
  private UserReadService userReadService;

  public UserQueryService(UserReadService userReadService) {
    this.userReadService = userReadService;
  }

  public Optional<UserData> findById(Long id) {
    return Optional.ofNullable(userReadService.findById(id));
  }
}
