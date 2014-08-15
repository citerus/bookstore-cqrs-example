package se.citerus.cqrs.bookstore.application.web.transport;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class RegisterPublisherRequest extends TransportObject {

  @NotNull
  public final String publisherContractId;
  @NotNull
  public final String publisherName;
  @Min(1)
  public final double fee;
  @Min(1)
  public final long limit;

  public RegisterPublisherRequest(@JsonProperty("publisherContractId") String publisherContractId,
                                  @JsonProperty("publisherName") String publisherName,
                                  @JsonProperty("fee") double fee,
                                  @JsonProperty("limit") long limit) {
    this.publisherContractId = publisherContractId;
    this.publisherName = publisherName;
    this.fee = fee;
    this.limit = limit;
  }

}
