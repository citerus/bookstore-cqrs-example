package se.citerus.cqrs.bookstore.shopping.domain;

import se.citerus.cqrs.bookstore.domain.ValueObject;

public class Item extends ValueObject {

  public final ProductId productId;
  public final String title;
  public final long price;

  public Item(ProductId productId, String title, long price) {
    this.productId = productId;
    this.title = title;
    this.price = price;
  }

}
