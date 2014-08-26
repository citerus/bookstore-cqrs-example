package se.citerus.cqrs.bookstore.admin.web.transport;

import java.util.List;

public class Order {

  public IdDto orderId;
  public long orderPlacedTimestamp;
  public long orderAmount;
  public String customerName;
  public List<OrderLine> orderLines;
  public String status;

}
