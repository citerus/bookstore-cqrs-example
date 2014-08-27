package se.citerus.cqrs.bookstore.admin.web.transport;

import se.citerus.cqrs.bookstore.TransportObject;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class CreateBookRequest extends TransportObject {

  @NotNull
  public String bookId;
  @NotNull
  public String isbn;
  @NotNull
  public String title;
  @NotNull
  public String description;
  @Min(1)
  public long price;
  /**
   * Optional.
   */
  public String publisherContractId;

}
