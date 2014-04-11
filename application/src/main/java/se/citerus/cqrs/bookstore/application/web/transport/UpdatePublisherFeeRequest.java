package se.citerus.cqrs.bookstore.application.web.transport;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Min;

public class UpdatePublisherFeeRequest extends TransportObject {

  @NotEmpty
  public final String publisherId;

  @Min(1)
  public final long fee;


  public UpdatePublisherFeeRequest(@JsonProperty("publisherId") String publisherId,
                                   @JsonProperty("fee") long fee) {
    this.publisherId = publisherId;
    this.fee = fee;
  }
}
