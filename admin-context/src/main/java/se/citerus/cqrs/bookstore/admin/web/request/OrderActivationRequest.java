package se.citerus.cqrs.bookstore.admin.web.request;

import se.citerus.cqrs.bookstore.TransportObject;

import javax.validation.constraints.NotNull;

public class OrderActivationRequest extends TransportObject {

  @NotNull
  public String orderId;

}
