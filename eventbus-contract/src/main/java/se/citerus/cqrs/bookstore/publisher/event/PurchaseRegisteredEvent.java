package se.citerus.cqrs.bookstore.publisher.event;

import se.citerus.cqrs.bookstore.book.BookId;
import se.citerus.cqrs.bookstore.event.DomainEvent;
import se.citerus.cqrs.bookstore.publisher.PublisherContractId;

public class PurchaseRegisteredEvent extends DomainEvent<PublisherContractId> {

  public final BookId bookId;
  public final long amount;

  public PurchaseRegisteredEvent(PublisherContractId contractId, int version, long timestamp, BookId bookId, long amount) {
    super(contractId, version, timestamp);
    this.bookId = bookId;
    this.amount = amount;
  }

}
