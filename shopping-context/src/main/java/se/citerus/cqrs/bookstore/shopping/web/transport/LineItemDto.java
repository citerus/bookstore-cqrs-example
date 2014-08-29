package se.citerus.cqrs.bookstore.shopping.web.transport;

import se.citerus.cqrs.bookstore.TransportObject;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class LineItemDto extends TransportObject {

  @NotNull
  public String bookId;

  @NotNull
  public String title;

  @Min(1)
  public long price;

  @Min(1)
  public int quantity;

  @Min(1)
  public long totalPrice;

}
