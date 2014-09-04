package se.citerus.cqrs.bookstore.shopping.domain;

import se.citerus.cqrs.bookstore.domain.ValueObject;

import java.util.UUID;

public class ProductId extends ValueObject {

  public final String id;

  public ProductId(String id) {
    this.id = id;
  }

  public static ProductId randomId() {
    return new ProductId(UUID.randomUUID().toString());
  }

}
