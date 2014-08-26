package se.citerus.cqrs.bookstore.book.event;

import se.citerus.cqrs.bookstore.book.BookId;
import se.citerus.cqrs.bookstore.event.DomainEvent;

public class BookPriceUpdatedEvent extends DomainEvent<BookId> {

  public final long oldPrice;
  public final long newPrice;

  public BookPriceUpdatedEvent(BookId id, int version, long timestamp, long oldPrice, long newPrice) {
    super(id, version, timestamp);
    this.oldPrice = oldPrice;
    this.newPrice = newPrice;
  }

}
