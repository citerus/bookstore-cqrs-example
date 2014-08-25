package se.citerus.cqrs.bookstore.order.publisher.domain;

import se.citerus.cqrs.bookstore.book.BookId;
import se.citerus.cqrs.bookstore.domain.AggregateRoot;
import se.citerus.cqrs.bookstore.publisher.PublisherContractId;
import se.citerus.cqrs.bookstore.publisher.event.PublisherRegisteredEvent;
import se.citerus.cqrs.bookstore.publisher.event.PurchaseRegisteredEvent;

import static com.google.common.base.Preconditions.checkState;

public class PublisherContract extends AggregateRoot<PublisherContractId> {

  // TODO: Extract feePercentage and limit to an AccumulatedFee class.
  private double feePercentage;
  private long limit;
  private Fee accumulatedFee;

  public void register(PublisherContractId publisherContractId, String name, double feePercentage, long limit) {
    assertHasNotBeenRegistered();
    applyChange(new PublisherRegisteredEvent(publisherContractId, nextVersion(), now(), name, feePercentage, limit));
  }

  public void registerPurchase(BookId bookId, long purchaseAmount) {
    Fee purchaseFee = accumulatedFee.calculateNextPurchaseFee(purchaseAmount, limit, feePercentage);
    // TODO: calculate accumulated and pass in event
    applyChange(new PurchaseRegisteredEvent(id(), nextVersion(), now(), bookId, purchaseAmount, purchaseFee.feeAmount()));
  }

  private void assertHasNotBeenRegistered() {
    checkState(id == null, "Contract has already been registered");
  }

  @SuppressWarnings("UnusedDeclaration")
  void handleEvent(PublisherRegisteredEvent event) {
    this.id = event.aggregateId;
    this.version = event.version;
    this.timestamp = event.timestamp;
    this.feePercentage = event.feePercentage;
    this.limit = event.limit;
    this.accumulatedFee = Fee.ZERO;
  }

  @SuppressWarnings("UnusedDeclaration")
  void handleEvent(PurchaseRegisteredEvent event) {
    this.version = event.version;
    this.timestamp = event.timestamp;
    // TODO: Change to simple assignment
    this.accumulatedFee = accumulatedFee.add(new Fee(event.feeAmount));
  }

}
