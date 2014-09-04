package se.citerus.cqrs.bookstore.ordercontext.publishercontract.event;

import com.fasterxml.jackson.annotation.JsonProperty;
import se.citerus.cqrs.bookstore.event.DomainEvent;
import se.citerus.cqrs.bookstore.ordercontext.order.ProductId;
import se.citerus.cqrs.bookstore.ordercontext.publishercontract.PublisherContractId;

public class PurchaseRegisteredEvent extends DomainEvent<PublisherContractId> {

  public final ProductId productId;
  public final long purchaseAmount;
  public final long feeAmount;
  public final long accumulatedFee;

  public PurchaseRegisteredEvent(@JsonProperty("aggregateId") PublisherContractId aggregateId,
                                 @JsonProperty("version") int version,
                                 @JsonProperty("timestamp") long timestamp,
                                 @JsonProperty("productId") ProductId productId,
                                 @JsonProperty("purchaseAmount") long purchaseAmount,
                                 @JsonProperty("feeAmount") long feeAmount,
                                 @JsonProperty("accumulatedFee") long accumulatedFee) {
    super(aggregateId, version, timestamp);
    this.productId = productId;
    this.purchaseAmount = purchaseAmount;
    this.feeAmount = feeAmount;
    this.accumulatedFee = accumulatedFee;
  }

}
