package se.citerus.cqrs.bookstore.event;

import se.citerus.cqrs.bookstore.GenericId;
import se.citerus.cqrs.bookstore.domain.AggregateRoot;

import java.util.List;

public interface DomainEventStore {

  List<DomainEvent> loadEvents(GenericId id);

  void save(GenericId id, Class<? extends AggregateRoot> type, List<DomainEvent> events);

  List<DomainEvent> getAllEvents();

}

