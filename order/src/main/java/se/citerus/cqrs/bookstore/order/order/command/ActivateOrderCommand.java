package se.citerus.cqrs.bookstore.order.order.command;

import se.citerus.cqrs.bookstore.command.Command;
import se.citerus.cqrs.bookstore.order.OrderId;

import static com.google.common.base.Preconditions.checkArgument;

public class ActivateOrderCommand extends Command {
  public final OrderId orderId;

  public ActivateOrderCommand(OrderId orderId) {
    checkArgument(orderId != null, "OrderId cannot be null");

    this.orderId = orderId;
  }

}
