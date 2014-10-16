package se.citerus.cqrs.bookstore.ordercontext.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import se.citerus.cqrs.bookstore.TransportObject;
import se.citerus.cqrs.bookstore.ordercontext.publishercontract.PublisherContractId;

public class OrderLine extends TransportObject {

  public final ProductId productId;
  public final String title;
  public final int quantity;
  public final long unitPrice;
  public final PublisherContractId publisherContractId;

  public OrderLine(ProductId productId, String title, int quantity, long unitPrice) {
    this(productId, title, quantity, unitPrice, null);
  }

  public OrderLine(@JsonProperty("productId") ProductId productId,
                   @JsonProperty("title") String title,
                   @JsonProperty("quantity") int quantity,
                   @JsonProperty("unitPrice") long unitPrice,
                   @JsonProperty("publisherContractId") PublisherContractId publisherContractId) {
    this.productId = productId;
    this.title = title;
    this.quantity = quantity;
    this.unitPrice = unitPrice;
    this.publisherContractId = publisherContractId;
  }

  public OrderLine withPublisher(PublisherContractId publisherContractId) {
    return new OrderLine(this.productId, this.title, this.quantity, this.unitPrice, publisherContractId);
  }

}
