package se.citerus.cqrs.bookstore.domain.order;

import se.citerus.cqrs.bookstore.domain.AggregateRoot;
import se.citerus.cqrs.bookstore.order.CustomerInformation;
import se.citerus.cqrs.bookstore.order.OrderId;
import se.citerus.cqrs.bookstore.order.OrderStatus;
import se.citerus.cqrs.bookstore.order.event.OrderActivatedEvent;
import se.citerus.cqrs.bookstore.order.event.OrderPlacedEvent;

import static com.google.common.base.Preconditions.checkState;

public class Order extends AggregateRoot<OrderId> {

  private OrderStatus status;
  private OrderLines orderLines;

  public void place(OrderId orderId, CustomerInformation customerInformation, OrderLines orderLines) {
    assertHasNotBeenPlaced();
    applyChange(new OrderPlacedEvent(orderId, nextVersion(), now(), customerInformation, orderLines.lines(),
        orderLines.totalAmount()));
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

  @SuppressWarnings("UnusedDeclaration")
  void handleEvent(OrderPlacedEvent event) {
    this.id = event.aggregateId;
    this.version = event.version;
    this.timestamp = event.timestamp;
    this.status = OrderStatus.PLACED;
    this.orderLines = new OrderLines(event.orderLines);
  }

  @SuppressWarnings("UnusedDeclaration")
  void handleEvent(OrderActivatedEvent event) {
    this.status = OrderStatus.ACTIVATED;
  }

  public OrderLines orderLines() {
    return orderLines;
  }

}
