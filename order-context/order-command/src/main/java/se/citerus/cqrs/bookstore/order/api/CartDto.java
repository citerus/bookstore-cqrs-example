package se.citerus.cqrs.bookstore.order.api;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.List;

import static se.citerus.cqrs.bookstore.GenericId.ID_PATTERN;

public class CartDto {

  @NotEmpty
  @Pattern(regexp = ID_PATTERN)
  public String cartId;

  @Min(1)
  public long totalPrice;

  @Min(1)
  public int totalQuantity;

  @NotNull
  @NotEmpty
  public List<LineItemDto> lineItems;

}
