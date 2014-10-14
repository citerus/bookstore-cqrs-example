package se.citerus.cqrs.bookstore.productcatalog.domain;

import se.citerus.cqrs.bookstore.domain.ValueObject;

public class Book extends ValueObject {

  public final String bookId;
  public final String isbn;
  public final String title;
  public final String description;

  public Book(String bookId, String isbn, String title, String description) {
    this.bookId = bookId;
    this.isbn = isbn;
    this.title = title;
    this.description = description;
  }

}
