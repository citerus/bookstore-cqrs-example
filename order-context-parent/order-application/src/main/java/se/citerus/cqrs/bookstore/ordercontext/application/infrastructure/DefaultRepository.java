package se.citerus.cqrs.bookstore.ordercontext.application.infrastructure;

import se.citerus.cqrs.bookstore.GenericId;
import se.citerus.cqrs.bookstore.domain.AggregateRoot;
import se.citerus.cqrs.bookstore.domain.Repository;
import se.citerus.cqrs.bookstore.event.DomainEvent;
import se.citerus.cqrs.bookstore.event.DomainEventBus;
import se.citerus.cqrs.bookstore.event.DomainEventStore;

import java.util.List;

import static java.lang.String.format;

public class DefaultRepository implements Repository {

  private final DomainEventBus domainEventBus;
  private final DomainEventStore domainEventStore;

  public DefaultRepository(DomainEventBus domainEventBus, DomainEventStore domainEventStore) {
    this.domainEventBus = domainEventBus;
    this.domainEventStore = domainEventStore;
  }

  @Override
  public <AR extends AggregateRoot> void save(AR aggregateRoot) {
    if (aggregateRoot.hasUncommittedEvents()) {
      List<DomainEvent> newEvents = aggregateRoot.getUncommittedEvents();
      domainEventStore.save(aggregateRoot.id(), aggregateRoot.getClass(), newEvents);
      domainEventBus.publish(newEvents);
    }
  }

  @Override
  public <AR extends AggregateRoot, ID extends GenericId> AR load(ID id, Class<AR> aggregateType) {
    try {
      AR aggregateRoot = aggregateType.newInstance();
      aggregateRoot.loadFromHistory(domainEventStore.loadEvents(id));
      return aggregateRoot;
    } catch (IllegalArgumentException iae) {
      String message = format("Aggregate of type [%s] does not exist, ID: %s", aggregateType.getSimpleName(), id);
      throw new IllegalArgumentException(message);
    } catch (Exception ex) {
      throw new RuntimeException(ex);
    }
  }

}
