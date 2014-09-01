package se.citerus.cqrs.bookstore.ordercontext.order.event;

import com.fasterxml.jackson.annotation.JsonProperty;
import se.citerus.cqrs.bookstore.event.DomainEvent;
import se.citerus.cqrs.bookstore.ordercontext.order.OrderId;

public class OrderActivatedEvent extends DomainEvent<OrderId> {

  public OrderActivatedEvent(@JsonProperty("aggregateId") OrderId aggregateId,
                             @JsonProperty("version") int version,
                             @JsonProperty("timestamp") long timestamp) {
    super(aggregateId, version, timestamp);
  }

}
