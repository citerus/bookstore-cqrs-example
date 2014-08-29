package se.citerus.cqrs.bookstore.shopping.web.request;

import org.hibernate.validator.constraints.NotEmpty;
import se.citerus.cqrs.bookstore.TransportObject;
import se.citerus.cqrs.bookstore.shopping.web.transport.CartDto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import static se.citerus.cqrs.bookstore.GenericId.ID_PATTERN;

public class PlaceOrderRequest extends TransportObject {

  @NotNull
  public CartDto cart;

  @NotEmpty
  @Pattern(regexp = ID_PATTERN)
  public String orderId;

  @NotNull
  public String customerName;

  @NotNull
  public String customerEmail;

  @NotNull
  public String customerAddress;

}
