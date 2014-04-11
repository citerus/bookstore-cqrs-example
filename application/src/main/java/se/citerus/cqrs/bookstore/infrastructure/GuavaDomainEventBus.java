package se.citerus.cqrs.bookstore.infrastructure;

import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.citerus.cqrs.bookstore.event.DomainEvent;
import se.citerus.cqrs.bookstore.event.DomainEventBus;
import se.citerus.cqrs.bookstore.event.DomainEventListener;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GuavaDomainEventBus implements DomainEventBus {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  private final EventBus eventBus;
  private final EventBus replayBus;

  public GuavaDomainEventBus() {
    ExecutorService executorService = Executors.newFixedThreadPool(1);
    eventBus = new AsyncEventBus("eventbus", executorService);
    replayBus = new AsyncEventBus("replaybus", executorService);
  }

  @Override
  public <T extends DomainEventListener> T register(T listener) {
    if (listener.supportsReplay()) {
      replayBus.register(listener);
    }
    eventBus.register(listener);
    return listener;
  }

  @Override
  public void publish(List<DomainEvent> events) {
    for (DomainEvent event : events) {
      eventBus.post(event);
    }
  }

  @Override
  public void republish(List<DomainEvent> events) {
    logger.info("Replaying [{}] events", events.size());
    for (DomainEvent event : events) {
      replayBus.post(event);
    }
  }

}
