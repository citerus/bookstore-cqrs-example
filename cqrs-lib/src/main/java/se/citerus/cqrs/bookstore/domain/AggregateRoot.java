package se.citerus.cqrs.bookstore.domain;

import se.citerus.cqrs.bookstore.GenericId;
import se.citerus.cqrs.bookstore.event.DomainEvent;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Base class for aggregate roots.
 */
public class AggregateRoot<T extends GenericId> {

  private final List<DomainEvent> uncommittedEvents = new ArrayList<>();

  private T id;
  private int version = 0;
  private long timestamp = 0;

  public List<DomainEvent> getUncommittedEvents() {
    return uncommittedEvents;
  }

  public void loadFromHistory(List<DomainEvent> history) {
    for (DomainEvent event : history) {
      applyChange(event, false);
    }
  }

  protected int nextVersion() {
    return version + 1;
  }

  protected long now() {
    return System.currentTimeMillis();
  }

  public T id() {
    return id;
  }

  public int version() {
    return version;
  }

  public long timestamp() {
    return timestamp;
  }

  protected void applyChange(DomainEvent event) {
    applyChange(event, true);
  }

  private void applyChange(DomainEvent event, boolean isNew) {
    updateMetadata(event);
    invokeHandlerMethod(event);
    if (isNew) uncommittedEvents.add(event);
  }

  private void updateMetadata(DomainEvent event) {
    this.id = (T) event.aggregateId;
    this.version = event.version;
    this.timestamp = event.timestamp;
  }

  private void invokeHandlerMethod(DomainEvent event) {
    try {
      Method method = getClass().getDeclaredMethod("handleEvent", event.getClass());
      method.setAccessible(true);
      method.invoke(this, event);
    } catch (Exception e) {
      throw new RuntimeException("Unable to call event handler method for " + event.getClass().getName(), e);
    }
  }

  public boolean hasUncommittedEvents() {
    return !uncommittedEvents.isEmpty();
  }

}
