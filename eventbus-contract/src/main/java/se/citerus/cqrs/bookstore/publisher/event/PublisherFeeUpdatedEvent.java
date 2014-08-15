package se.citerus.cqrs.bookstore.publisher.event;

import se.citerus.cqrs.bookstore.event.DomainEvent;
import se.citerus.cqrs.bookstore.publisher.PublisherContractId;

public class PublisherFeeUpdatedEvent extends DomainEvent<PublisherContractId> {

  public final double oldFee;
  public final double newFee;

  public PublisherFeeUpdatedEvent(PublisherContractId contractId, int version, long timestamp, double oldFee, double newFee) {
    super(contractId, version, timestamp);
    this.oldFee = oldFee;
    this.newFee = newFee;
  }

}
