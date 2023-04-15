package icu.ngte.udma.core.domain.entity;

import com.google.common.base.Objects;

public abstract class UUIDBasedEntity implements Entity {

  @Override
  public abstract EntityId getId();

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UUIDBasedEntity uuidBased = (UUIDBasedEntity) o;
    return Objects.equal(getId(), uuidBased.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(getId());
  }

  @Override
  public String toString() {
    return "Entity[" + getId().getEntityType() + ", " + getId().toString() + "]";
  }
}
