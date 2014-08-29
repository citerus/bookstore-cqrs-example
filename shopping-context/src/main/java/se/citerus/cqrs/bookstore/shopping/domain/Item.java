package se.citerus.cqrs.bookstore.shopping.domain;

import se.citerus.cqrs.bookstore.domain.ValueObject;

public class Item extends ValueObject {

  public final BookId bookId;
  public final String title;
  public final long price;

  public Item(BookId bookId, String title, long price) {
    this.bookId = bookId;
    this.title = title;
    this.price = price;
  }

}
