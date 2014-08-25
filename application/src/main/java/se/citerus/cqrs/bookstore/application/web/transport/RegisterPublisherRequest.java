package se.citerus.cqrs.bookstore.application.web.transport;

import com.fasterxml.jackson.annotation.JsonProperty;
import se.citerus.cqrs.bookstore.TransportObject;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class RegisterPublisherRequest extends TransportObject {

  @NotNull
  public final String publisherContractId;
  @NotNull
  public final String publisherName;
  @Min(1)
  public final double feePercentage;
  @Min(1)
  public final long limit;

  public RegisterPublisherRequest(@JsonProperty("publisherContractId") String publisherContractId,
                                  @JsonProperty("publisherName") String publisherName,
                                  @JsonProperty("feePercentage") double feePercentage,
                                  @JsonProperty("limit") long limit) {
    this.publisherContractId = publisherContractId;
    this.publisherName = publisherName;
    this.feePercentage = feePercentage;
    this.limit = limit;
  }

}
