package se.citerus.cqrs.bookstore.shopping.web.model;

import se.citerus.cqrs.bookstore.ValueObject;

import java.util.UUID;

public class BookId extends ValueObject {

  public final String id;

  public BookId(String id) {
    this.id = id;
  }

  public static BookId randomId() {
    return new BookId(UUID.randomUUID().toString());
  }

}
