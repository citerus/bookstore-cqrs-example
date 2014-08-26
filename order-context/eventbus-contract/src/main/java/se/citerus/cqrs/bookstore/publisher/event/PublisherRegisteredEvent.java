package se.citerus.cqrs.bookstore.publisher.event;

import se.citerus.cqrs.bookstore.event.DomainEvent;
import se.citerus.cqrs.bookstore.publisher.PublisherContractId;

public class PublisherRegisteredEvent extends DomainEvent<PublisherContractId> {

  public final String publisherName;
  public final double feePercentage;
  public final long limit;

  public PublisherRegisteredEvent(PublisherContractId publisherContractId, int version, long timestamp, String publisherName,
                                  double feePercentage, long limit) {
    super(publisherContractId, version, timestamp);
    this.publisherName = publisherName;
    this.feePercentage = feePercentage;
    this.limit = limit;
  }

}
