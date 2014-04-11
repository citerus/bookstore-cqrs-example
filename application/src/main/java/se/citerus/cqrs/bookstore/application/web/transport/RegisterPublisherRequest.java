package se.citerus.cqrs.bookstore.application.web.transport;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class RegisterPublisherRequest extends TransportObject {

  @NotNull
  public final String publisherId;
  @NotNull
  public final String publisherName;
  @Min(1)
  public final double fee;

  public RegisterPublisherRequest(@JsonProperty("publisherId") String publisherId,
                                  @JsonProperty("publisherName") String publisherName,
                                  @JsonProperty("fee") double fee) {
    this.publisherId = publisherId;
    this.publisherName = publisherName;
    this.fee = fee;
  }

}
