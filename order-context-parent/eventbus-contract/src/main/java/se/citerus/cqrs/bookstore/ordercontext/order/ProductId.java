package se.citerus.cqrs.bookstore.ordercontext.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import se.citerus.cqrs.bookstore.GenericId;

import java.util.UUID;

public class ProductId extends GenericId {

  public ProductId(@JsonProperty("id") String id) {
    super(id);
  }

  public static ProductId randomId() {
    return new ProductId(UUID.randomUUID().toString());
  }

}
