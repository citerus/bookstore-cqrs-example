package se.citerus.cqrs.bookstore.order.event;

import se.citerus.cqrs.bookstore.event.DomainEvent;
import se.citerus.cqrs.bookstore.order.CustomerInformation;
import se.citerus.cqrs.bookstore.order.OrderId;
import se.citerus.cqrs.bookstore.order.OrderLine;

import java.util.Collections;
import java.util.List;

public class OrderPlacedEvent extends DomainEvent<OrderId> {

  public final CustomerInformation customerInformation;
  public final List<OrderLine> orderLines;
  public final long orderAmount;

  public OrderPlacedEvent(OrderId id, int version, long timestamp, CustomerInformation customerInformation,
                          List<OrderLine> orderLines, long orderAmount) {
    super(id, version, timestamp);
    this.customerInformation = customerInformation;
    this.orderLines = Collections.unmodifiableList(orderLines);
    this.orderAmount = orderAmount;
  }

}
