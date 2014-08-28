package se.citerus.cqrs.bookstore.shopping.web.transport;

import se.citerus.cqrs.bookstore.TransportObject;

import javax.validation.constraints.NotNull;

public class AddItemRequest extends TransportObject {

  @NotNull
  public String bookId;

}
