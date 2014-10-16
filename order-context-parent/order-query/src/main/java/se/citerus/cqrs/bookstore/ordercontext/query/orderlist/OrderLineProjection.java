package se.citerus.cqrs.bookstore.ordercontext.query.orderlist;

import se.citerus.cqrs.bookstore.ordercontext.order.ProductId;
import se.citerus.cqrs.bookstore.ordercontext.publishercontract.PublisherContractId;

public class OrderLineProjection {

  public ProductId productId;

  public String title;

  public int quantity;

  public long unitPrice;

  public PublisherContractId publisherContractId;

}
