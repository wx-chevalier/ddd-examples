package com.msjc.realworld.application.service;

import com.msjc.realworld.domain.user.UserDO;
import com.msjc.realworld.infra.dmr.data.ProfileData;
import com.msjc.realworld.infra.dmr.data.UserData;
import com.msjc.realworld.infra.dmr.readservice.UserReadService;
import com.msjc.realworld.infra.dmr.readservice.UserRelationshipReadService;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProfileQueryService {
  private UserReadService userReadService;
  private UserRelationshipReadService userRelationshipReadService;

  @Autowired
  public ProfileQueryService(
      UserReadService userReadService, UserRelationshipReadService userRelationshipReadService) {
    this.userReadService = userReadService;
    this.userRelationshipReadService = userRelationshipReadService;
  }

  public Optional<ProfileData> findByUsername(String username, UserDO currentUser) {
    UserData userData = userReadService.findByUsername(username);
    if (userData == null) {
      return Optional.empty();
    } else {
      ProfileData profileData =
          new ProfileData(
              userData.getId(),
              userData.getUsername(),
              userData.getBio(),
              userData.getImage(),
              userRelationshipReadService.isUserFollowing(currentUser.getId(), userData.getId()));
      return Optional.of(profileData);
    }
  }
}
