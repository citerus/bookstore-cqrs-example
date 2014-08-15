package se.citerus.cqrs.bookstore.domain.book;

import se.citerus.cqrs.bookstore.book.BookId;
import se.citerus.cqrs.bookstore.book.event.BookCreatedEvent;
import se.citerus.cqrs.bookstore.book.event.BookPriceUpdatedEvent;
import se.citerus.cqrs.bookstore.domain.AggregateRoot;
import se.citerus.cqrs.bookstore.publisher.PublisherContractId;

public class Book extends AggregateRoot<BookId> {

  private long price;

  public void create(BookId bookId, String isbn, String title, String description, long price, PublisherContractId publisherContractId) {
    applyChange(new BookCreatedEvent(bookId, nextVersion(), now(), isbn, title, description, price, publisherContractId));
  }

  public void updatePrice(long updatedPrice) {
    applyChange(new BookPriceUpdatedEvent(id, nextVersion(), now(), this.price, updatedPrice));
  }

  @SuppressWarnings("UnusedDeclaration")
  void handleEvent(BookCreatedEvent event) {
    this.id = event.aggregateId;
    this.version = event.version;
    this.timestamp = event.timestamp;
    this.price = event.price;
  }

  @SuppressWarnings("UnusedDeclaration")
  void handleEvent(BookPriceUpdatedEvent event) {
    this.version = event.version;
    this.timestamp = event.timestamp;
    this.price = event.newPrice;
  }

}
