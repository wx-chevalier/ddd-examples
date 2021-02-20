package com.udma.core.domain.publisher;

import com.udma.core.domain.event.DomainEvent;
import io.vavr.collection.List;

public interface EventStorage {

  void save(DomainEvent event);

  List<DomainEvent> toPublish();

  void published(List<DomainEvent> events);
}
