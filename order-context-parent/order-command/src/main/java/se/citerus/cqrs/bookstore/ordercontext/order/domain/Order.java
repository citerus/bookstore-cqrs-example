package se.citerus.cqrs.bookstore.ordercontext.order.domain;

import se.citerus.cqrs.bookstore.domain.AggregateRoot;
import se.citerus.cqrs.bookstore.ordercontext.order.CustomerInformation;
import se.citerus.cqrs.bookstore.ordercontext.order.OrderId;
import se.citerus.cqrs.bookstore.ordercontext.order.OrderLine;
import se.citerus.cqrs.bookstore.ordercontext.order.OrderStatus;
import se.citerus.cqrs.bookstore.ordercontext.order.event.OrderActivatedEvent;
import se.citerus.cqrs.bookstore.ordercontext.order.event.OrderPlacedEvent;

import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;

public class Order extends AggregateRoot<OrderId> {

  private OrderStatus status;

  public void place(OrderId orderId, CustomerInformation customerInformation, List<OrderLine> orderLines, long totalAmount) {
    assertHasNotBeenPlaced();
    assertMoreThanZeroOrderLines(orderLines);
    applyChange(new OrderPlacedEvent(orderId, nextVersion(), now(), customerInformation, orderLines, totalAmount));
  }

  public void activate() {
    if (orderIsPlaced()) {
      applyChange(new OrderActivatedEvent(id, nextVersion(), now()));
    }
  }

  private boolean orderIsPlaced() {
    return status == OrderStatus.PLACED;
  }

  private void assertMoreThanZeroOrderLines(List<OrderLine> orderLines) {
    checkArgument(!orderLines.isEmpty(), "Cannot place an order without any order lines");
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
  }

  @SuppressWarnings("UnusedDeclaration")
  void handleEvent(OrderActivatedEvent event) {
    this.version = event.version;
    this.timestamp = event.timestamp;
    this.status = OrderStatus.ACTIVATED;
  }

}
