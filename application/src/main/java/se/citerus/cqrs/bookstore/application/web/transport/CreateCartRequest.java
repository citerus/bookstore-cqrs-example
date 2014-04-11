package se.citerus.cqrs.bookstore.application.web.transport;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;

public class CreateCartRequest extends TransportObject {

  @NotNull
  public final String cartId;

  public CreateCartRequest(@JsonProperty("cartId") String cartId) {
    this.cartId = cartId;
  }

}
