package se.citerus.cqrs.bookstore.admin.web.transport;

import se.citerus.cqrs.bookstore.TransportObject;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class UpdateBookPriceRequest extends TransportObject {

  @NotNull
  public String bookId;
  @Min(1)
  public long price;

}
