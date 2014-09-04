package se.citerus.cqrs.bookstore.shopping.client.productcatalog;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import se.citerus.cqrs.bookstore.TransportObject;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductDto extends TransportObject {

  public BookDto book;

  public long price;

}
