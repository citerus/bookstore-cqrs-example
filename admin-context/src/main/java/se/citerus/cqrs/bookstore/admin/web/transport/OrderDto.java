package se.citerus.cqrs.bookstore.admin.web.transport;

import se.citerus.cqrs.bookstore.TransportObject;

import java.util.List;

public class OrderDto extends TransportObject {

  public IdDto orderId;

  public long orderPlacedTimestamp;

  public long orderAmount;

  public String customerName;

  public List<OrderLineDto> orderLines;

  public String status;

}
