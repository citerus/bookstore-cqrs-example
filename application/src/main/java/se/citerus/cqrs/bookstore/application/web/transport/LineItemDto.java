package se.citerus.cqrs.bookstore.application.web.transport;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class LineItemDto extends TransportObject {

  @NotNull
  public final String bookId;
  @NotNull
  public final String title;
  @Min(1)
  public final long price;
  @Min(1)
  public final int quantity;
  @Min(1)
  public final long totalPrice;

  public LineItemDto(@JsonProperty("bookId") String bookId,
                     @JsonProperty("title") String title,
                     @JsonProperty("price") long price,
                     @JsonProperty("quantity") int quantity,
                     @JsonProperty("totalPrice") long totalPrice) {
    this.bookId = bookId;
    this.title = title;
    this.price = price;
    this.quantity = quantity;
    this.totalPrice = totalPrice;
  }

}
