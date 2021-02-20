package com.udma.core.domain.entity;

import com.google.common.base.Objects;
import java.io.Serializable;
import java.util.UUID;

public abstract class UUIDBasedEntityId implements Serializable {

  private static final long serialVersionUID = 1L;

  public abstract UUID getId();

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UUIDBasedEntityId uuidBased = (UUIDBasedEntityId) o;
    return Objects.equal(getId(), uuidBased.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(getId());
  }

  @Override
  public String toString() {
    return getId().toString();
  }
}
