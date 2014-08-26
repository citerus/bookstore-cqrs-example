package se.citerus.cqrs.bookstore.order.web.transport;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class LineItemDto {

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
