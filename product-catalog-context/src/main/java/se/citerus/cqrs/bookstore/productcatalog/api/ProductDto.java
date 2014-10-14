package se.citerus.cqrs.bookstore.productcatalog.api;

import org.hibernate.validator.constraints.NotEmpty;
import se.citerus.cqrs.bookstore.TransportObject;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;

import static se.citerus.cqrs.bookstore.GenericId.ID_PATTERN;

public class ProductDto extends TransportObject {

  @NotEmpty
  @Pattern(regexp = ID_PATTERN)
  public String productId;

  @Valid
  public BookDto book;

  @Min(0)
  public long price;

  @NotEmpty
  @Pattern(regexp = ID_PATTERN)
  public String publisherContractId;

}
