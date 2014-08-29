package se.citerus.cqrs.bookstore.admin.web.api;

import org.hibernate.validator.constraints.NotEmpty;
import se.citerus.cqrs.bookstore.TransportObject;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import static se.citerus.cqrs.bookstore.GenericId.ID_PATTERN;

public class CreateBookRequest extends TransportObject {

  @NotEmpty
  @Pattern(regexp = ID_PATTERN)
  public String bookId;

  @NotNull
  public String isbn;

  @NotNull
  public String title;

  @NotNull
  public String description;

  @Min(1)
  public long price;

  @NotEmpty
  @Pattern(regexp = ID_PATTERN)
  public String publisherContractId;

}
