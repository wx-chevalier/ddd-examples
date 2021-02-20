package com.msjc.analytics.common.data;

import com.msjc.domain.EntityId;
import java.util.UUID;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RequiredArgsConstructor
@ToString(of = {"id"})
public class PersonId implements EntityId {

  @Getter final UUID id;

  @Override
  public String getEntityType() {
    return "PERSON";
  }
}
