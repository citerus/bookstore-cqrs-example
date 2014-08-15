package se.citerus.cqrs.bookstore.domain.publisher;

import se.citerus.cqrs.bookstore.book.BookId;
import se.citerus.cqrs.bookstore.domain.AggregateRoot;
import se.citerus.cqrs.bookstore.publisher.PublisherContractId;
import se.citerus.cqrs.bookstore.publisher.event.PublisherFeeUpdatedEvent;
import se.citerus.cqrs.bookstore.publisher.event.PublisherRegisteredEvent;
import se.citerus.cqrs.bookstore.publisher.event.PurchaseRegisteredEvent;

import static com.google.common.base.Preconditions.checkState;

public class PublisherContract extends AggregateRoot<PublisherContractId> {

  private String publisherName;
  private double fee;

  public void register(PublisherContractId contractId, String name, double fee) {
    assertHasNotBeenRegistered();
    applyChange(new PublisherRegisteredEvent(contractId, nextVersion(), now(), name, fee));
  }

  public void updateFee(double newFee) {
    applyChange(new PublisherFeeUpdatedEvent(id(), nextVersion(), now(), fee, newFee));
  }

  public void registerPurchase(BookId bookId, long amount) {
    applyChange(new PurchaseRegisteredEvent(id(), nextVersion(), now(), bookId, amount));
  }

  private void assertHasNotBeenRegistered() {
    checkState(id == null, "Contract has already been registered");
  }

  @SuppressWarnings("UnusedDeclaration")
  void handleEvent(PublisherRegisteredEvent event) {
    this.id = event.aggregateId;
    this.version = event.version;
    this.timestamp = event.timestamp;
    this.publisherName = event.publisherName;
    this.fee = event.fee;
  }

  @SuppressWarnings("UnusedDeclaration")
  void handleEvent(PublisherFeeUpdatedEvent event) {
    this.version = event.version;
    this.timestamp = event.timestamp;
    this.fee = event.newFee;
  }

  @SuppressWarnings("UnusedDeclaration")
  void handleEvent(PurchaseRegisteredEvent event) {
    this.version = event.version;
    this.timestamp = event.timestamp;
  }

  public String publisherName() {
    return publisherName;
  }

  public double fee() {
    return fee;
  }

}
