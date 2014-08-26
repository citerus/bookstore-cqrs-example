package se.citerus.cqrs.bookstore.bookcatalog;

public class Book {

  private final String bookId;
  private final String isbn;
  private final String title;
  private final String description;
  private final double price;
  private final String publisherContractId;

  public Book(String bookId, String isbn, String title, String description, long price, String publisherContractId) {
    this.bookId = bookId;
    this.isbn = isbn;
    this.title = title;
    this.description = description;
    this.price = price;
    this.publisherContractId = publisherContractId;
  }
}
