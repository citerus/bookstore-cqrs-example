package se.citerus.cqrs.bookstore.publisher;

import com.fasterxml.jackson.annotation.JsonProperty;
import se.citerus.cqrs.bookstore.GenericId;

import java.util.UUID;

public class PublisherId extends GenericId {

  public PublisherId(@JsonProperty("id") String id) {
    super(id);
  }

  public static PublisherId randomId() {
    return new PublisherId(UUID.randomUUID().toString());
  }

}
