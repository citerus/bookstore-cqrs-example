package se.citerus.cqrs.bookstore.admin.web.transport;

import com.fasterxml.jackson.annotation.JsonProperty;
import se.citerus.cqrs.bookstore.TransportObject;

import javax.validation.constraints.NotNull;

public class OrderActivationRequest extends TransportObject {

  @NotNull
  public final String orderId;

  public OrderActivationRequest(@JsonProperty("orderId") String orderId) {
    this.orderId = orderId;
  }

}
