package se.citerus.cqrs.bookstore.application;

import com.google.common.eventbus.EventBus;
import org.junit.Ignore;
import se.citerus.cqrs.bookstore.event.DomainEvent;
import se.citerus.cqrs.bookstore.event.DomainEventBus;
import se.citerus.cqrs.bookstore.event.DomainEventListener;

import java.util.List;

@Ignore
public class TestEventBus implements DomainEventBus {

  private final EventBus eventBus = new EventBus("testbus");

  @Override
  public void publish(List<DomainEvent> events) {
    for (DomainEvent event : events) {
      System.out.println("Publishing: " + event);
      eventBus.post(event);
    }
  }

  @Override
  public void republish(List<DomainEvent> events) {
  }

  @Override
  public <T extends DomainEventListener> T register(T listener) {
    eventBus.register(listener);
    return listener;
  }

}
