package icu.ngte.udma.core.domain.publisher;

import icu.ngte.udma.core.domain.event.DomainEvent;
import icu.ngte.udma.core.domain.event.DomainEvents;
import io.vavr.collection.List;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;

@AllArgsConstructor
public class StoreAndForwardDomainEventPublisher implements DomainEvents {

  private final DomainEvents eventsPublisher;
  private final EventStorage eventsStorage;

  @Override
  public void publish(DomainEvent event) {
    eventsStorage.save(event);
  }

  @Scheduled(fixedRate = 3000L)
  @Transactional
  public void publishAllPeriodically() {
    List<DomainEvent> domainEvents = eventsStorage.toPublish();
    domainEvents.forEach(eventsPublisher::publish);
    eventsStorage.published(domainEvents);
  }
}
