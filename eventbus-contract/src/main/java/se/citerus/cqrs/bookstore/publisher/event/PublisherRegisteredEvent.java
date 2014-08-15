package se.citerus.cqrs.bookstore.publisher.event;

import se.citerus.cqrs.bookstore.event.DomainEvent;
import se.citerus.cqrs.bookstore.publisher.PublisherContractId;

public class PublisherRegisteredEvent extends DomainEvent<PublisherContractId> {

  public final String publisherName;
  public final double fee;
  public final long limit;

  public PublisherRegisteredEvent(PublisherContractId publisherContractId, int version, long timestamp, String publisherName,
                                  double fee, long limit) {
    super(publisherContractId, version, timestamp);
    this.publisherName = publisherName;
    this.fee = fee;
    this.limit = limit;
  }

}
