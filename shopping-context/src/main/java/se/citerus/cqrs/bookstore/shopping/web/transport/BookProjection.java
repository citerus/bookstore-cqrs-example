package se.citerus.cqrs.bookstore.shopping.web.transport;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BookProjection {
  public String title;
  public long price;
}
