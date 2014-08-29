package se.citerus.cqrs.bookstore.shopping.web.api;

import se.citerus.cqrs.bookstore.TransportObject;

public class LineItemDto extends TransportObject {

  public String bookId;

  public String title;

  public long price;

  public int quantity;

  public long totalPrice;

}
