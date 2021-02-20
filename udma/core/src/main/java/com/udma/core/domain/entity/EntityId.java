package com.udma.core.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.udma.core.type.model.HasId;
import java.io.Serializable;

public interface EntityId extends HasId<Long>, Serializable {
  @JsonIgnore
  default boolean isNullId() {
    return getId() == 0L;
  }

  public abstract String getEntityType();
}
