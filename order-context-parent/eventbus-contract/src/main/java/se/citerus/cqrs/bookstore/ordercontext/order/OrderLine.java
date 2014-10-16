package se.citerus.cqrs.bookstore.ordercontext.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import se.citerus.cqrs.bookstore.TransportObject;

public class OrderLine extends TransportObject {

  public final ProductId productId;
  public final String title;
  public final int quantity;
  public final long unitPrice;

  public OrderLine(@JsonProperty("productId") ProductId productId,
                   @JsonProperty("title") String title,
                   @JsonProperty("quantity") int quantity,
                   @JsonProperty("unitPrice") long unitPrice) {
    this.productId = productId;
    this.title = title;
    this.quantity = quantity;
    this.unitPrice = unitPrice;
  }

}
