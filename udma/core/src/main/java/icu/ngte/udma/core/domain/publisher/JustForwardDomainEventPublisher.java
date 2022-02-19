package icu.ngte.udma.core.domain.publisher;

import icu.ngte.udma.core.domain.event.DomainEvent;
import icu.ngte.udma.core.domain.event.DomainEvents;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;

@AllArgsConstructor
public class JustForwardDomainEventPublisher implements DomainEvents {

  private final ApplicationEventPublisher applicationEventPublisher;

  @Override
  public void publish(DomainEvent event) {
    applicationEventPublisher.publishEvent(event);
  }
}
