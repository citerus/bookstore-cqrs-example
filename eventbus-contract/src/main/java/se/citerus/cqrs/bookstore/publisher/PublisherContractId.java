package se.citerus.cqrs.bookstore.publisher;

import com.fasterxml.jackson.annotation.JsonProperty;
import se.citerus.cqrs.bookstore.GenericId;

import java.util.UUID;

public class PublisherContractId extends GenericId {

  public PublisherContractId(@JsonProperty("id") String id) {
    super(id);
  }

  public static PublisherContractId randomId() {
    return new PublisherContractId(UUID.randomUUID().toString());
  }

}
