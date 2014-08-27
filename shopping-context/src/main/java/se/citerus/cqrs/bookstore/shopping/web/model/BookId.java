package se.citerus.cqrs.bookstore.shopping.web.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import se.citerus.cqrs.bookstore.TransportObject;

import java.util.UUID;

public class BookId extends TransportObject {

  public final String id;

  public BookId(@JsonProperty("id") String id) {
    this.id = id;
  }

  public static BookId randomId() {
    return new BookId(UUID.randomUUID().toString());
  }

}
