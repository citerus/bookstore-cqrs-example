package se.citerus.cqrs.bookstore.ordercontext.order.event;

import com.fasterxml.jackson.annotation.JsonProperty;
import se.citerus.cqrs.bookstore.event.DomainEvent;
import se.citerus.cqrs.bookstore.ordercontext.order.CustomerInformation;
import se.citerus.cqrs.bookstore.ordercontext.order.OrderId;
import se.citerus.cqrs.bookstore.ordercontext.order.OrderLine;

import java.util.Collections;
import java.util.List;

public class OrderPlacedEvent extends DomainEvent<OrderId> {

  public final CustomerInformation customerInformation;
  public final List<OrderLine> orderLines;
  public final long orderAmount;

  public OrderPlacedEvent(@JsonProperty("aggregateId") OrderId id,
                          @JsonProperty("version") int version,
                          @JsonProperty("timestamp") long timestamp,
                          @JsonProperty("customerInformation") CustomerInformation customerInformation,
                          @JsonProperty("orderLines") List<OrderLine> orderLines,
                          @JsonProperty("orderAmount") long orderAmount) {
    super(id, version, timestamp);
    this.customerInformation = customerInformation;
    this.orderLines = Collections.unmodifiableList(orderLines);
    this.orderAmount = orderAmount;
  }

}
