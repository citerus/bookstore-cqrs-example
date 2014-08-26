package se.citerus.cqrs.bookstore.order.web.transport;

import javax.validation.constraints.NotNull;

public class PlaceOrderRequest {

  @NotNull
  public String orderId;
  @NotNull
  public String customerName;
  @NotNull
  public String customerEmail;
  @NotNull
  public String customerAddress;
  @NotNull
  public CartDto cart;

}
