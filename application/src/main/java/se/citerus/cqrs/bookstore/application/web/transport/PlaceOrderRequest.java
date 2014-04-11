package se.citerus.cqrs.bookstore.application.web.transport;

import com.fasterxml.jackson.annotation.JsonProperty;
import se.citerus.cqrs.bookstore.order.OrderId;

import javax.validation.constraints.NotNull;

public class PlaceOrderRequest extends TransportObject {

  @NotNull
  public final String cartId;
  @NotNull
  public final String orderId;
  @NotNull
  public final String customerName;
  @NotNull
  public final String customerEmail;
  @NotNull
  public final String customerAddress;

  public PlaceOrderRequest(@JsonProperty("cartId") String cartId,
                           @JsonProperty("orderId") String orderId,
                           @JsonProperty("customerName") String customerName,
                           @JsonProperty("customerEmail") String customerEmail,
                           @JsonProperty("customerAddress") String customerAddress) {

    this.cartId = cartId;
    this.orderId = orderId;
    this.customerName = customerName;
    this.customerEmail = customerEmail;
    this.customerAddress = customerAddress;
  }

}
