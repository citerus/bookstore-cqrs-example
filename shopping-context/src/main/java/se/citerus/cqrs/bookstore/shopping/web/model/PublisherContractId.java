package se.citerus.cqrs.bookstore.shopping.web.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public class PublisherContractId {

  public final String id;

  public PublisherContractId(@JsonProperty("id") String id) {
    this.id = id;
  }

  public static PublisherContractId randomId() {
    return new PublisherContractId(UUID.randomUUID().toString());
  }

}
