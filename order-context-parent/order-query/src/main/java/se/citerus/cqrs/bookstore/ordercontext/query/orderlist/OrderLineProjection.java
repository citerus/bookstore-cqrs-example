package se.citerus.cqrs.bookstore.ordercontext.query.orderlist;

import se.citerus.cqrs.bookstore.ordercontext.order.BookId;
import se.citerus.cqrs.bookstore.ordercontext.publishercontract.PublisherContractId;

public class OrderLineProjection {

  public BookId bookId;
  public String title;
  public int quantity;
  public long unitPrice;
  public PublisherContractId publisherContractId;

  public boolean bookHasRegisteredPublisher() {
    return publisherContractId != null;
  }

  // TODO: Calculate this on event?
  public long amount() {
    return unitPrice * quantity;
  }

}
