package se.citerus.cqrs.bookstore.infrastructure;

import se.citerus.cqrs.bookstore.GenericId;
import se.citerus.cqrs.bookstore.domain.AggregateRoot;
import se.citerus.cqrs.bookstore.domain.Repository;
import se.citerus.cqrs.bookstore.event.DomainEvent;
import se.citerus.cqrs.bookstore.event.DomainEventBus;
import se.citerus.cqrs.bookstore.event.DomainEventStore;

import java.util.List;

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
      aggregateRoot.markChangesAsCommitted();
    }
  }

  @Override
  public <AR extends AggregateRoot, ID extends GenericId> AR load(ID id, Class<AR> aggregateType) {
    try {
      AR aggregateRoot = aggregateType.newInstance();
      aggregateRoot.loadFromHistory(domainEventStore.loadEvents(id));
      return aggregateRoot;
    } catch (IllegalArgumentException iae) {
      throw new IllegalArgumentException(String.format("Aggregate of type [%s] does not exist, ID: %s", aggregateType.getSimpleName(), id));
    } catch (Exception ex) {
      throw new RuntimeException(ex);
    }
  }

}
