package se.citerus.cqrs.bookstore.domain.publisher;

import se.citerus.cqrs.bookstore.book.BookId;
import se.citerus.cqrs.bookstore.domain.AggregateRoot;
import se.citerus.cqrs.bookstore.publisher.PublisherId;
import se.citerus.cqrs.bookstore.publisher.event.PublisherFeeUpdatedEvent;
import se.citerus.cqrs.bookstore.publisher.event.PublisherRegisteredEvent;
import se.citerus.cqrs.bookstore.publisher.event.PurchaseRegisteredEvent;

public class Publisher extends AggregateRoot<PublisherId> {

  private String name;
  private double fee;

  public void register(PublisherId publisherId, String name, double fee) {
    applyChange(new PublisherRegisteredEvent(publisherId, nextVersion(), now(), name, fee));
  }

  public void updateFee(double newFee) {
    applyChange(new PublisherFeeUpdatedEvent(id(), nextVersion(), now(), fee, newFee));
  }

  public void registerPurchase(BookId bookId, long totalPrice) {
    applyChange(new PurchaseRegisteredEvent(id(), nextVersion(), now(), bookId, totalPrice));
  }

  @SuppressWarnings("UnusedDeclaration")
  void handleEvent(PublisherRegisteredEvent event) {
    this.id = event.aggregateId;
    this.version = event.version;
    this.timestamp = event.timestamp;
    this.name = event.publisherName;
    this.fee = event.fee;
  }

  @SuppressWarnings("UnusedDeclaration")
  void handleEvent(PublisherFeeUpdatedEvent event) {
    this.id = event.aggregateId;
    this.version = event.version;
    this.timestamp = event.timestamp;
    this.fee = event.newFee;
  }

  @SuppressWarnings("UnusedDeclaration")
  void handleEvent(PurchaseRegisteredEvent event) {
    this.id = event.aggregateId;
    this.version = event.version;
    this.timestamp = event.timestamp;
  }

  public String name() {
    return name;
  }

  public double fee() {
    return fee;
  }

}
