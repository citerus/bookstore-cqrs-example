package se.citerus.cqrs.bookstore.order;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import se.citerus.cqrs.bookstore.ValueObject;
import se.citerus.cqrs.bookstore.book.BookId;
import se.citerus.cqrs.bookstore.publisher.PublisherContractId;

public class OrderLine extends ValueObject {

  public final BookId bookId;
  public final String title;
  public final int quantity;
  public final long unitPrice;
  public final PublisherContractId publisherContractId;

  public OrderLine(BookId bookId, String title, int quantity, long unitPrice) {
    this(bookId, title, quantity, unitPrice, null);
  }

  @JsonCreator
  public OrderLine(@JsonProperty("bookId") BookId bookId,
                   @JsonProperty("title") String title,
                   @JsonProperty("quantity") int quantity,
                   @JsonProperty("price") long unitPrice,
                   @JsonProperty("publisherContractId") PublisherContractId publisherContractId) {
    this.bookId = bookId;
    this.title = title;
    this.quantity = quantity;
    this.unitPrice = unitPrice;
    this.publisherContractId = publisherContractId;
  }

  public OrderLine withPublisher(PublisherContractId publisherContractId) {
    return new OrderLine(this.bookId, this.title, this.quantity, this.unitPrice, publisherContractId);
  }

  public long amount() {
    return unitPrice * quantity;
  }

  public boolean bookHasRegisteredPublisher() {
    return publisherContractId != null;
  }

}
