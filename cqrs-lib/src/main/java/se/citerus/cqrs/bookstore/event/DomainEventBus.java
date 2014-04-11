package se.citerus.cqrs.bookstore.event;

import java.util.List;

public interface DomainEventBus {

  void publish(List<DomainEvent> events);

  void republish(List<DomainEvent> events);

  <T extends DomainEventListener> T register(T listener);

}
