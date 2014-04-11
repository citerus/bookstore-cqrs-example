package se.citerus.cqrs.bookstore.query;

import com.fasterxml.jackson.annotation.JsonProperty;
import se.citerus.cqrs.bookstore.publisher.PublisherId;

public class PublisherProjection extends Projection {

  private PublisherId publisherId;
  private String name;
  private double fee;

  public PublisherProjection(@JsonProperty("publisherId") PublisherId publisherId,
                             @JsonProperty("name") String name,
                             @JsonProperty("fee") double fee) {
    this.publisherId = publisherId;
    this.name = name;
    this.fee = fee;
  }

  public PublisherId getPublisherId() {
    return publisherId;
  }

  public String getName() {
    return name;
  }

  public double getFee() {
    return fee;
  }

}
