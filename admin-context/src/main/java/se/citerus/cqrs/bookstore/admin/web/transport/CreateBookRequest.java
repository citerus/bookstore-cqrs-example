package se.citerus.cqrs.bookstore.admin.web.transport;

import com.fasterxml.jackson.annotation.JsonProperty;
import se.citerus.cqrs.bookstore.TransportObject;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class CreateBookRequest extends TransportObject {

  @NotNull
  public final String bookId;
  @NotNull
  public final String isbn;
  @NotNull
  public final String title;
  @NotNull
  public final String description;
  @Min(1)
  public final long price;
  /**
   * Optional.
   */
  public final String publisherContractId;

  public CreateBookRequest(@JsonProperty("bookId") String bookId,
                           @JsonProperty("isbn") String isbn,
                           @JsonProperty("title") String title,
                           @JsonProperty("description") String description,
                           @JsonProperty("price") long price,
                           @JsonProperty("publisherContractId") String publisherContractId) {
    this.bookId = bookId;
    this.isbn = isbn;
    this.title = title;
    this.description = description;
    this.price = price;
    this.publisherContractId = publisherContractId;
  }

}
