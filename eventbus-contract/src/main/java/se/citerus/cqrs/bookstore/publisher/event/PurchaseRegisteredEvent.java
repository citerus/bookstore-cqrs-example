package se.citerus.cqrs.bookstore.publisher.event;

import se.citerus.cqrs.bookstore.book.BookId;
import se.citerus.cqrs.bookstore.event.DomainEvent;
import se.citerus.cqrs.bookstore.publisher.PublisherContractId;

public class PurchaseRegisteredEvent extends DomainEvent<PublisherContractId> {

  public final BookId bookId;
  public final long purchaseAmount;
  public final long feeAmount;

  public PurchaseRegisteredEvent(PublisherContractId publisherContractId, int version, long timestamp, BookId bookId,
                                 long purchaseAmount, long feeAmount) {
    super(publisherContractId, version, timestamp);
    this.bookId = bookId;
    this.purchaseAmount = purchaseAmount;
    this.feeAmount = feeAmount;
  }

}
