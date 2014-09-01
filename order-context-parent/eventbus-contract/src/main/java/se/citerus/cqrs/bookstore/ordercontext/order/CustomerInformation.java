package se.citerus.cqrs.bookstore.ordercontext.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import se.citerus.cqrs.bookstore.domain.ValueObject;

public class CustomerInformation extends ValueObject {

  public final String customerName;
  public final String email;
  public final String address;

  public CustomerInformation(@JsonProperty("customerName") String customerName,
                             @JsonProperty("email") String email,
                             @JsonProperty("address") String address) {
    this.customerName = customerName;
    this.email = email;
    this.address = address;
  }

}
