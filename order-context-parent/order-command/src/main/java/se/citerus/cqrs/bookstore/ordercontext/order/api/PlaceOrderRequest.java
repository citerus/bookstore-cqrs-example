package se.citerus.cqrs.bookstore.ordercontext.order.api;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import static se.citerus.cqrs.bookstore.GenericId.ID_PATTERN;

public class PlaceOrderRequest {

  @NotEmpty
  @Pattern(regexp = ID_PATTERN)
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
