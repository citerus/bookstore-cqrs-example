package se.citerus.cqrs.bookstore.saga;

import se.citerus.cqrs.bookstore.event.DomainEventListener;

public abstract class Saga implements DomainEventListener {

  @Override
  public boolean supportsReplay() {
    return false;
  }

}
