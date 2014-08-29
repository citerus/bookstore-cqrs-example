package se.citerus.cqrs.bookstore.shopping.web.api;

import se.citerus.cqrs.bookstore.TransportObject;

import java.util.List;

public class CartDto extends TransportObject {

  public String cartId;

  public long totalPrice;

  public int totalQuantity;

  public List<LineItemDto> lineItems;

}
