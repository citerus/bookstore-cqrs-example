package se.citerus.cqrs.bookstore.domain.order;

import se.citerus.cqrs.bookstore.ValueObject;
import se.citerus.cqrs.bookstore.order.OrderLine;

import java.util.Collections;
import java.util.List;

public class OrderLines extends ValueObject {

  public static final OrderLines EMPTY = new OrderLines(Collections.<OrderLine>emptyList());

  private final List<OrderLine> lines;

  public OrderLines(List<OrderLine> orderLines) {
    this.lines = Collections.unmodifiableList(orderLines);
  }

  public long totalAmount() {
    long totalAmount = 0;
    for (OrderLine orderLine : lines) {
      totalAmount += orderLine.lineCost();
    }
    return totalAmount;
  }

  public List<OrderLine> lines() {
    return Collections.unmodifiableList(lines);
  }

}
