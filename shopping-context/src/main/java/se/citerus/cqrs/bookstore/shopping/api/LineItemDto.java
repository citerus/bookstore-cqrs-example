package se.citerus.cqrs.bookstore.shopping.api;

import se.citerus.cqrs.bookstore.TransportObject;

public class LineItemDto extends TransportObject {

  public String productId;

  public String title;

  public long price;

  public int quantity;

  public long totalPrice;

}
