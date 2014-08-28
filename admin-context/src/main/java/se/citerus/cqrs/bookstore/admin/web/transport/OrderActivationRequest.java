package se.citerus.cqrs.bookstore.admin.web.transport;

import se.citerus.cqrs.bookstore.TransportObject;

import javax.validation.constraints.NotNull;

public class OrderActivationRequest extends TransportObject {

  @NotNull
  public String orderId;

}
