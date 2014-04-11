package se.citerus.cqrs.bookstore.domain.order;

import se.citerus.cqrs.bookstore.domain.AggregateRoot;
import se.citerus.cqrs.bookstore.order.CustomerInformation;
import se.citerus.cqrs.bookstore.order.OrderId;
import se.citerus.cqrs.bookstore.order.OrderLine;
import se.citerus.cqrs.bookstore.order.OrderStatus;
import se.citerus.cqrs.bookstore.order.event.OrderActivatedEvent;
import se.citerus.cqrs.bookstore.order.event.OrderPlacedEvent;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkState;

public class Order extends AggregateRoot<OrderId> {

  private OrderStatus status;
  private final List<OrderLine> orderLines = new ArrayList<>();

  public void place(OrderId orderId, CustomerInformation customerInformation, List<OrderLine> orderLines) {
    assertHasNotBeenPlaced();
    applyChange(new OrderPlacedEvent(orderId, nextVersion(), now(), customerInformation, orderLines, sum(orderLines)));
  }

  public void activate() {
    if (orderIsPlaced()) {
      applyChange(new OrderActivatedEvent(id, nextVersion(), now()));
    }
  }

  private boolean orderIsPlaced() {
    return status == OrderStatus.PLACED;
  }

  public OrderStatus status() {
    return status;
  }

  private void assertHasNotBeenPlaced() {
    checkState(id == null, "Order has already been placed");
  }

  private long sum(List<OrderLine> orderLines) {
    long totalAmount = 0;
    for (OrderLine orderLine : orderLines) {
      totalAmount += orderLine.lineCost();
    }
    return totalAmount;
  }

  @SuppressWarnings("UnusedDeclaration")
  void handleEvent(OrderPlacedEvent event) {
    this.id = event.aggregateId;
    this.version = event.version;
    this.timestamp = event.timestamp;
    this.status = OrderStatus.PLACED;
    this.orderLines.addAll(event.orderLines);
  }

  @SuppressWarnings("UnusedDeclaration")
  void handleEvent(OrderActivatedEvent event) {
    this.status = OrderStatus.ACTIVATED;
  }

  public List<OrderLine> orderLines() {
    return orderLines;
  }

}
