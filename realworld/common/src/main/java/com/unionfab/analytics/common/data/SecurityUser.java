package com.msjc.analytics.common.data;

import io.vavr.collection.List;
import io.vavr.control.Option;

public interface SecurityUser {

  String getUsername();

  String getNickname();

  Option<TenantId> getTenantId();

  Option<UserId> getUserId();

  Option<PersonId> getPersonId();

  List<String> getAuthorities();
}
