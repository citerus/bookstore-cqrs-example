package se.citerus.cqrs.bookstore.publisher.event;

import se.citerus.cqrs.bookstore.event.DomainEvent;
import se.citerus.cqrs.bookstore.publisher.PublisherContractId;

public class PublisherRegisteredEvent extends DomainEvent<PublisherContractId> {

  public final String publisherName;
  public final double fee;

  public PublisherRegisteredEvent(PublisherContractId contractId, int version, long timestamp, String publisherName, double fee) {
    super(contractId, version, timestamp);
    this.publisherName = publisherName;
    this.fee = fee;
  }

}
