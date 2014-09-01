package se.citerus.cqrs.bookstore.ordercontext.publishercontract.event;

import com.fasterxml.jackson.annotation.JsonProperty;
import se.citerus.cqrs.bookstore.event.DomainEvent;
import se.citerus.cqrs.bookstore.ordercontext.publishercontract.PublisherContractId;

public class PublisherContractRegisteredEvent extends DomainEvent<PublisherContractId> {

  public final String publisherName;
  public final double feePercentage;
  public final long limit;

  public PublisherContractRegisteredEvent(@JsonProperty("aggregateId") PublisherContractId aggregateId,
                                          @JsonProperty("version") int version,
                                          @JsonProperty("timestamp") long timestamp,
                                          @JsonProperty("publisherName") String publisherName,
                                          @JsonProperty("feePercentage") double feePercentage,
                                          @JsonProperty("limit") long limit) {
    super(aggregateId, version, timestamp);
    this.publisherName = publisherName;
    this.feePercentage = feePercentage;
    this.limit = limit;
  }

}
