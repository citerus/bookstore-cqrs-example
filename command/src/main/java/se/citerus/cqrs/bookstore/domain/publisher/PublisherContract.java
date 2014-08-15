package se.citerus.cqrs.bookstore.domain.publisher;

import se.citerus.cqrs.bookstore.book.BookId;
import se.citerus.cqrs.bookstore.domain.AggregateRoot;
import se.citerus.cqrs.bookstore.publisher.PublisherContractId;
import se.citerus.cqrs.bookstore.publisher.event.PublisherRegisteredEvent;
import se.citerus.cqrs.bookstore.publisher.event.PurchaseRegisteredEvent;

import static com.google.common.base.Preconditions.checkState;

public class PublisherContract extends AggregateRoot<PublisherContractId> {

  private double feePercentage;
  private long limit;
  private long accumulatedFeeAmount;

  public void register(PublisherContractId publisherContractId, String name, double feePercentage, long limit) {
    assertHasNotBeenRegistered();
    applyChange(new PublisherRegisteredEvent(publisherContractId, nextVersion(), now(), name, feePercentage, limit));
  }

  public void registerPurchase(BookId bookId, long purchaseAmount) {
    long feeAmount = capAmount(calculateFee(purchaseAmount));
    applyChange(new PurchaseRegisteredEvent(id(), nextVersion(), now(), bookId, purchaseAmount, feeAmount));
  }

  private long capAmount(long amount) {
    long newAmount = amount;
    if (exceedsLimit(amount)) {
      newAmount = limit - accumulatedFeeAmount;
    }
    return newAmount;
  }

  protected long calculateFee(long purchaseAmount) {
    // TODO: Implement!
    return purchaseAmount;
  }

  private boolean exceedsLimit(long amount) {
    return accumulatedFeeAmount + amount >= limit;
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
  }

  @SuppressWarnings("UnusedDeclaration")
  void handleEvent(PurchaseRegisteredEvent event) {
    this.version = event.version;
    this.timestamp = event.timestamp;
    this.accumulatedFeeAmount += event.feeAmount;
  }

}
