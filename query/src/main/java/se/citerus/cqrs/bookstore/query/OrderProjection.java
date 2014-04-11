package se.citerus.cqrs.bookstore.query;

import com.fasterxml.jackson.annotation.JsonProperty;
import se.citerus.cqrs.bookstore.order.OrderId;
import se.citerus.cqrs.bookstore.order.OrderLine;
import se.citerus.cqrs.bookstore.order.OrderStatus;

import java.util.ArrayList;
import java.util.List;

public class OrderProjection extends Projection {

  private final OrderId orderId;
  private final long orderPlacedTimestamp;
  private final long orderAmount;
  private final String customerName;
  private final List<OrderLine> orderLines;
  private OrderStatus status;

  public OrderProjection(@JsonProperty("orderId") OrderId orderId,
                         @JsonProperty("orderPlacedTimestamp") long orderPlacedTimestamp,
                         @JsonProperty("customerName") String customerName,
                         @JsonProperty("orderAmount") long orderAmount,
                         @JsonProperty("orderLines") List<OrderLine> orderLines,
                         @JsonProperty("status") OrderStatus status) {
    this.orderId = orderId;
    this.orderPlacedTimestamp = orderPlacedTimestamp;
    this.customerName = customerName;
    this.orderAmount = orderAmount;
    this.orderLines = new ArrayList<>(orderLines);
    this.status = status;
  }

  public List<OrderLine> getOrderLines() {
    return orderLines;
  }

  public OrderId getOrderId() {
    return orderId;
  }

  public String getCustomerName() {
    return customerName;
  }

  public long getOrderAmount() {
    return orderAmount;
  }

  public OrderStatus getStatus() {
    return status;
  }

  public void setStatus(OrderStatus status) {
    this.status = status;
  }

  public long getOrderPlacedTimestamp() {
    return orderPlacedTimestamp;
  }

}
