package se.citerus.cqrs.bookstore.ordercontext.publishercontract.event;

import com.fasterxml.jackson.annotation.JsonProperty;
import se.citerus.cqrs.bookstore.event.DomainEvent;
import se.citerus.cqrs.bookstore.ordercontext.order.BookId;
import se.citerus.cqrs.bookstore.ordercontext.publishercontract.PublisherContractId;

public class PurchaseRegisteredEvent extends DomainEvent<PublisherContractId> {

  public final BookId bookId;
  public final long purchaseAmount;
  public final double feeAmount;

  public PurchaseRegisteredEvent(@JsonProperty("aggregateId") PublisherContractId aggregateId,
                                 @JsonProperty("version") int version,
                                 @JsonProperty("timestamp") long timestamp,
                                 @JsonProperty("bookId") BookId bookId,
                                 @JsonProperty("purchaseAmount") long purchaseAmount,
                                 @JsonProperty("feeAmount") double feeAmount) {
    super(aggregateId, version, timestamp);
    this.bookId = bookId;
    this.purchaseAmount = purchaseAmount;
    this.feeAmount = feeAmount;
  }

}
