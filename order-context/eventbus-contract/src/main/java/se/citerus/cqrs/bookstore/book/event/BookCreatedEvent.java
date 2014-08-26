package se.citerus.cqrs.bookstore.book.event;

import se.citerus.cqrs.bookstore.book.BookId;
import se.citerus.cqrs.bookstore.event.DomainEvent;
import se.citerus.cqrs.bookstore.publisher.PublisherContractId;

public class BookCreatedEvent extends DomainEvent<BookId> {

  public final String isbn;
  public final String title;
  public final String description;
  public final long price;
  public final PublisherContractId publisherContractId;

  public BookCreatedEvent(BookId bookId, int version, long timestamp, String isbn, String title,
                          String description, long price, PublisherContractId publisherContractId) {
    super(bookId, version, timestamp);
    this.isbn = isbn;
    this.title = title;
    this.description = description;
    this.price = price;
    this.publisherContractId = publisherContractId;
  }

}
