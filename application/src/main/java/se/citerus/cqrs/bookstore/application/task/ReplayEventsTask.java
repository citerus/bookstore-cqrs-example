package se.citerus.cqrs.bookstore.application.task;

import com.google.common.collect.ImmutableMultimap;
import io.dropwizard.servlets.tasks.Task;
import se.citerus.cqrs.bookstore.event.DomainEventBus;
import se.citerus.cqrs.bookstore.event.DomainEventStore;

import java.io.PrintWriter;

public class ReplayEventsTask extends Task {

  private final DomainEventStore domainEventStore;
  private final DomainEventBus domainEventBus;

  public ReplayEventsTask(DomainEventStore domainEventStore, DomainEventBus domainEventBus) {
    super("republish-events");
    this.domainEventStore = domainEventStore;
    this.domainEventBus = domainEventBus;
  }

  @Override
  public void execute(ImmutableMultimap<String, String> parameters, PrintWriter output) throws Exception {
    domainEventBus.republish(domainEventStore.getAllEvents());
  }

}
