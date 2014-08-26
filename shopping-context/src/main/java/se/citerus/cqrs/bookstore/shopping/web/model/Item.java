package se.citerus.cqrs.bookstore.shopping.web.model;

import se.citerus.cqrs.bookstore.ValueObject;

public class Item extends ValueObject {

  public final String title;
  public final long price;
  public final BookId bookId;

  public Item(BookId bookId, String title, long price) {
    this.bookId = bookId;
    this.title = title;
    this.price = price;
  }

}
