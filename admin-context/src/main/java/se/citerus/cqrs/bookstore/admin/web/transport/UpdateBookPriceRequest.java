package se.citerus.cqrs.bookstore.admin.web.transport;

import com.fasterxml.jackson.annotation.JsonProperty;
import se.citerus.cqrs.bookstore.TransportObject;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class UpdateBookPriceRequest extends TransportObject {

  @NotNull
  public final String bookId;
  @Min(1)
  public final long price;

  public UpdateBookPriceRequest(@JsonProperty("bookId") String bookId, @JsonProperty("price") long price) {
    this.bookId = bookId;
    this.price = price;
  }

}
