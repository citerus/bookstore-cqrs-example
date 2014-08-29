package se.citerus.cqrs.bookstore.bookcatalog.api;

import org.hibernate.validator.constraints.NotEmpty;
import se.citerus.cqrs.bookstore.TransportObject;

import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;

import static se.citerus.cqrs.bookstore.GenericId.ID_PATTERN;

public class BookDto extends TransportObject {

  @NotEmpty
  @Pattern(regexp = ID_PATTERN)
  public String bookId;

  @NotEmpty
  public String isbn;

  @NotEmpty
  public String title;

  @NotEmpty
  public String description;

  @Min(0)
  public long price;

  @NotEmpty
  @Pattern(regexp = ID_PATTERN)
  public String publisherContractId;

}
