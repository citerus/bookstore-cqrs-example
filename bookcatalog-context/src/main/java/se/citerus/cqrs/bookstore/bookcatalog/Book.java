package se.citerus.cqrs.bookstore.bookcatalog;

public class Book {

  private final String bookId;
  private final String isbn;
  private final String title;
  private final String description;
  private final long price;
  private final String publisherContractId;

  public Book(String bookId, String isbn, String title, String description, long price, String publisherContractId) {
    this.bookId = bookId;
    this.isbn = isbn;
    this.title = title;
    this.description = description;
    this.price = price;
    this.publisherContractId = publisherContractId;
  }

  public String bookId() {
    return bookId;
  }

  public BookDto toDto() {
    BookDto bookDto = new BookDto();
    bookDto.bookId = bookId;
    bookDto.isbn = isbn;
    bookDto.title = title;
    bookDto.description = description;
    bookDto.price = price;
    bookDto.publisherContractId = publisherContractId;
    return bookDto;
  }
}
