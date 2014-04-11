package se.citerus.cqrs.bookstore.infrastructure;

import com.google.common.collect.Lists;
import se.citerus.cqrs.bookstore.GenericId;
import se.citerus.cqrs.bookstore.domain.AggregateRoot;
import se.citerus.cqrs.bookstore.event.DomainEvent;
import se.citerus.cqrs.bookstore.event.DomainEventStore;

import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;

public class InMemoryDomainEventStore implements DomainEventStore {

  private final Deque<DomainEvent> events = new ConcurrentLinkedDeque<>();

  @Override
  public synchronized List<DomainEvent> loadEvents(GenericId id) {
    List<DomainEvent> loadedEvents = new ArrayList<>();

    for (DomainEvent event : events) {
      if (event.aggregateId.equals(id)) {
        loadedEvents.add(event);
      }
    }
    if (loadedEvents.isEmpty()) throw new IllegalArgumentException("Aggregate does not exist: " + id);
    else return loadedEvents;
  }

  @Override
  public synchronized void save(GenericId id, Class<? extends AggregateRoot> type, List<DomainEvent> events) {
    this.events.addAll(events);
  }

  @Override
  public synchronized List<DomainEvent> getAllEvents() {
    return Lists.newArrayList(events.descendingIterator());
  }

}
