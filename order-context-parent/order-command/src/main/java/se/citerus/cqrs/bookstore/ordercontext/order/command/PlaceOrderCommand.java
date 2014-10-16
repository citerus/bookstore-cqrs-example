package se.citerus.cqrs.bookstore.ordercontext.order.command;

import se.citerus.cqrs.bookstore.command.Command;
import se.citerus.cqrs.bookstore.ordercontext.order.OrderId;
import se.citerus.cqrs.bookstore.ordercontext.order.domain.CustomerInformation;
import se.citerus.cqrs.bookstore.ordercontext.order.domain.OrderLine;

import java.util.Collections;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;

public class PlaceOrderCommand extends Command {

  public final OrderId orderId;
  public final CustomerInformation customerInformation;
  public final List<OrderLine> orderLines;
  public final long totalAmount;

  public PlaceOrderCommand(OrderId orderId, CustomerInformation customerInformation, List<OrderLine> orderLines, long totalAmount) {
    checkArgument(orderId != null, "OrderId cannot be null");
    checkArgument(customerInformation != null, "CustomerInformation cannot be null");
    checkArgument(orderLines != null, "Items cannot be null");
    checkArgument(!orderLines.isEmpty(), "Item list cannot be empty");
    checkArgument(totalAmount > 0, "Total amount must be > 0");
    this.orderId = orderId;
    this.customerInformation = customerInformation;
    this.orderLines = Collections.unmodifiableList(orderLines);
    this.totalAmount = totalAmount;
  }

}
