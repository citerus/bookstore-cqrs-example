package se.citerus.cqrs.bookstore.order.web.transport;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

public class CartDto {

  @NotNull
  public String cartId;
  @Min(1)
  public long totalPrice;
  @Min(1)
  public int totalQuantity;
  @NotNull
  public List<LineItemDto> lineItems;

}
