package icu.ngte.udma.core.domain.event;

import io.vavr.collection.List;
import java.util.UUID;

public interface DomainEvent extends Event {

  UUID getEventId();

  default List<DomainEvent> normalize() {
    return List.of(this);
  }
}
