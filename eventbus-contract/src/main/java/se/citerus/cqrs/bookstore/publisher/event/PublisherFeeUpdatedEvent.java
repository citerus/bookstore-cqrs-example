package se.citerus.cqrs.bookstore.publisher.event;

import se.citerus.cqrs.bookstore.event.DomainEvent;
import se.citerus.cqrs.bookstore.publisher.PublisherId;

public class PublisherFeeUpdatedEvent extends DomainEvent<PublisherId> {

  public final double oldFee;
  public final double newFee;

  public PublisherFeeUpdatedEvent(PublisherId publisherId, int version, long timestamp, double oldFee, double newFee) {
    super(publisherId, version, timestamp);
    this.oldFee = oldFee;
    this.newFee = newFee;
  }

}
