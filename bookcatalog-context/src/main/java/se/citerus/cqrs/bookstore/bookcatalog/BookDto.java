package se.citerus.cqrs.bookstore.bookcatalog;

import org.hibernate.validator.constraints.NotEmpty;
import se.citerus.cqrs.bookstore.TransportObject;

import javax.validation.constraints.Min;

public class BookDto extends TransportObject {

  @NotEmpty
  public String bookId;
  @NotEmpty
  public String isbn;
  @NotEmpty
  public String title;
  public String description;
  @Min(0)
  public long price;
  /**
   * Optional
   */
  public String publisherContractId;

}
