package se.citerus.cqrs.bookstore.event;

public interface DomainEventListener {

  boolean supportsReplay();

}
