package se.citerus.cqrs.bookstore.ordercontext.order.event;

import se.citerus.cqrs.bookstore.event.DomainEvent;
import se.citerus.cqrs.bookstore.ordercontext.order.OrderId;

public class OrderActivatedEvent extends DomainEvent<OrderId> {

  public OrderActivatedEvent(OrderId orderId, int version, long timestamp) {
    super(orderId, version, timestamp);
  }

}
