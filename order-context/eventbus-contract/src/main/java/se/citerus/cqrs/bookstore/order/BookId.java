package se.citerus.cqrs.bookstore.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import se.citerus.cqrs.bookstore.GenericId;

import java.util.UUID;

public class BookId extends GenericId {

  public BookId(@JsonProperty("id") String id) {
    super(id);
  }

  public static BookId randomId() {
    return new BookId(UUID.randomUUID().toString());
  }

}
