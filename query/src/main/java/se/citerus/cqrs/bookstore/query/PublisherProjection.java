package se.citerus.cqrs.bookstore.query;

import com.fasterxml.jackson.annotation.JsonProperty;
import se.citerus.cqrs.bookstore.publisher.PublisherContractId;

public class PublisherProjection extends Projection {

  private PublisherContractId contractId;
  private String name;
  private double fee;

  public PublisherProjection(@JsonProperty("publisherId") PublisherContractId contractId,
                             @JsonProperty("name") String name,
                             @JsonProperty("fee") double fee) {
    this.contractId = contractId;
    this.name = name;
    this.fee = fee;
  }

  public PublisherContractId getContractId() {
    return contractId;
  }

  public String getName() {
    return name;
  }

  public double getFee() {
    return fee;
  }

}
