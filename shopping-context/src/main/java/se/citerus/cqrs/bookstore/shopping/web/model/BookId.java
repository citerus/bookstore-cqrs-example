package se.citerus.cqrs.bookstore.shopping.web.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public class BookId {

  public final String id;

  public BookId(@JsonProperty("id") String id) {
    this.id = id;
  }

  public static BookId randomId() {
    return new BookId(UUID.randomUUID().toString());
  }

}
