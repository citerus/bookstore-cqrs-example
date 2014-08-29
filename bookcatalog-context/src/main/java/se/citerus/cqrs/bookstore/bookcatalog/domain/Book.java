package se.citerus.cqrs.bookstore.bookcatalog.domain;

import se.citerus.cqrs.bookstore.domain.ValueObject;

public class Book extends ValueObject {

  public final String bookId;
  public final String isbn;
  public final String title;
  public final String description;
  public final long price;
  public final String publisherContractId;

  public Book(String bookId, String isbn, String title, String description, long price, String publisherContractId) {
    this.bookId = bookId;
    this.isbn = isbn;
    this.title = title;
    this.description = description;
    this.price = price;
    this.publisherContractId = publisherContractId;
  }

}
