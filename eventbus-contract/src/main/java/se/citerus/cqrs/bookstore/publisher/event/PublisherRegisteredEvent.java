package se.citerus.cqrs.bookstore.publisher.event;

import se.citerus.cqrs.bookstore.event.DomainEvent;
import se.citerus.cqrs.bookstore.publisher.PublisherId;

public class PublisherRegisteredEvent extends DomainEvent<PublisherId> {

  public final String publisherName;
  public final double fee;

  public PublisherRegisteredEvent(PublisherId publisherId, int version, long timestamp, String publisherName, double fee) {
    super(publisherId, version, timestamp);
    this.publisherName = publisherName;
    this.fee = fee;
  }

}
