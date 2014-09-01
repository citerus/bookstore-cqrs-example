package se.citerus.cqrs.bookstore.ordercontext.publishercontract.domain;

import se.citerus.cqrs.bookstore.domain.AggregateRoot;
import se.citerus.cqrs.bookstore.ordercontext.order.BookId;
import se.citerus.cqrs.bookstore.ordercontext.publishercontract.PublisherContractId;
import se.citerus.cqrs.bookstore.ordercontext.publishercontract.event.PublisherContractRegisteredEvent;
import se.citerus.cqrs.bookstore.ordercontext.publishercontract.event.PurchaseRegisteredEvent;

import static com.google.common.base.Preconditions.checkState;

public class PublisherContract extends AggregateRoot<PublisherContractId> {

  private double feePercentage;
  private long limit;
  private long accumulatedFee;

  public void register(PublisherContractId publisherContractId, String name, double feePercentage, long limit) {
    assertHasNotBeenRegistered();
    applyChange(new PublisherContractRegisteredEvent(publisherContractId, nextVersion(), now(), name, feePercentage, limit));
  }

  public void registerPurchase(BookId bookId, long purchaseAmount) {
    AccumulatedFee newFee = new AccumulatedFee(accumulatedFee, limit, feePercentage).addPurchase(purchaseAmount);
    applyChange(new PurchaseRegisteredEvent(id(), nextVersion(), now(), bookId, purchaseAmount, newFee.lastPurchaseFee(), newFee.accumulatedFee()));
  }

  private void assertHasNotBeenRegistered() {
    checkState(id == null, "Contract has already been registered");
  }

  @SuppressWarnings("UnusedDeclaration")
  void handleEvent(PublisherContractRegisteredEvent event) {
    this.id = event.aggregateId;
    this.version = event.version;
    this.timestamp = event.timestamp;
    this.feePercentage = event.feePercentage;
    this.limit = event.limit;
    this.accumulatedFee = 0;
  }

  @SuppressWarnings("UnusedDeclaration")
  void handleEvent(PurchaseRegisteredEvent event) {
    this.version = event.version;
    this.timestamp = event.timestamp;
    this.accumulatedFee = event.accumulatedFee;
  }

}
