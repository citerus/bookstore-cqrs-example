package se.citerus.cqrs.bookstore.admin.web.transport;

import se.citerus.cqrs.bookstore.TransportObject;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class OrderLineDto extends TransportObject {

  @NotNull
  public IdDto bookId;
  @NotNull
  public String title;
  @Min(1)
  public int quantity;
  @Min(0)
  public long unitPrice;
  /**
   * Optional
   */
  public IdDto publisherContractId;

}
