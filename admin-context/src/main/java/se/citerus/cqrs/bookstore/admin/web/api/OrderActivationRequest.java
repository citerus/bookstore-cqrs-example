package se.citerus.cqrs.bookstore.admin.web.api;

import org.hibernate.validator.constraints.NotEmpty;
import se.citerus.cqrs.bookstore.TransportObject;

import javax.validation.constraints.Pattern;

import static se.citerus.cqrs.bookstore.GenericId.ID_PATTERN;

public class OrderActivationRequest extends TransportObject {

  @NotEmpty
  @Pattern(regexp = ID_PATTERN)
  public String orderId;

}
