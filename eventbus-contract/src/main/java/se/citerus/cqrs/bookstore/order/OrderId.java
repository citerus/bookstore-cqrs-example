package se.citerus.cqrs.bookstore.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import se.citerus.cqrs.bookstore.GenericId;

import java.util.UUID;

public class OrderId extends GenericId {

  public OrderId(@JsonProperty("id") String id) {
    super(id);
  }

  public static OrderId randomId() {
    return new OrderId(UUID.randomUUID().toString());
  }

}
