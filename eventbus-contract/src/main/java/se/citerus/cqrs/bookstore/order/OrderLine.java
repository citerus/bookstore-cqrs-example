package se.citerus.cqrs.bookstore.order;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import se.citerus.cqrs.bookstore.ValueObject;
import se.citerus.cqrs.bookstore.book.BookId;
import se.citerus.cqrs.bookstore.publisher.PublisherId;

public class OrderLine extends ValueObject {

  public final BookId bookId;
  public final String title;
  public final int quantity;
  public final long originalPrice;
  public final PublisherId publisherId;

  public OrderLine(BookId bookId, String title, int quantity, long price) {
    this(bookId, title, quantity, price, null);
  }

  @JsonCreator
  public OrderLine(@JsonProperty("bookId") BookId bookId,
                   @JsonProperty("title") String title,
                   @JsonProperty("quantity") int quantity,
                   @JsonProperty("price") long price,
                   @JsonProperty("publisherId") PublisherId publisherId) {
    this.bookId = bookId;
    this.title = title;
    this.quantity = quantity;
    this.originalPrice = price;
    this.publisherId = publisherId;
  }

  public OrderLine withPublisher(PublisherId publisherId) {
    return new OrderLine(this.bookId, this.title, this.quantity, this.originalPrice, publisherId);
  }

  public long lineCost() {
    return originalPrice * quantity;
  }

  public boolean bookHasRegisteredPublisher() {
    return publisherId != null;
  }

}
