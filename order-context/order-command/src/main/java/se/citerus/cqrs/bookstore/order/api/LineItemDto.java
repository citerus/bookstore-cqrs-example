package se.citerus.cqrs.bookstore.order.api;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import static se.citerus.cqrs.bookstore.GenericId.ID_PATTERN;

public class LineItemDto {

  @NotEmpty
  @Pattern(regexp = ID_PATTERN)
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
