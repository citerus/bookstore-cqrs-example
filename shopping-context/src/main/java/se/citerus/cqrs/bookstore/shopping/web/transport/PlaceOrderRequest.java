package se.citerus.cqrs.bookstore.shopping.web.transport;

import se.citerus.cqrs.bookstore.TransportObject;

import javax.validation.constraints.NotNull;

public class PlaceOrderRequest extends TransportObject {

  @NotNull
  public CartDto cart;
  @NotNull
  public String orderId;
  @NotNull
  public String customerName;
  @NotNull
  public String customerEmail;
  @NotNull
  public String customerAddress;

}
