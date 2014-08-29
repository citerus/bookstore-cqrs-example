package se.citerus.cqrs.bookstore.shopping.domain;

import se.citerus.cqrs.bookstore.domain.ValueObject;

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
