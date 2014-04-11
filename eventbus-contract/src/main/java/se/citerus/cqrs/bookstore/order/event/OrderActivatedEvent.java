package se.citerus.cqrs.bookstore.order.event;

import se.citerus.cqrs.bookstore.event.DomainEvent;
import se.citerus.cqrs.bookstore.order.OrderId;

public class OrderActivatedEvent extends DomainEvent<OrderId> {

  public OrderActivatedEvent(OrderId orderId, int version, long timestamp) {
    super(orderId, version, timestamp);
  }

}
