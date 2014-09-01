package se.citerus.cqrs.bookstore.ordercontext.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import se.citerus.cqrs.bookstore.TransportObject;

public class CustomerInformation extends TransportObject {

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
