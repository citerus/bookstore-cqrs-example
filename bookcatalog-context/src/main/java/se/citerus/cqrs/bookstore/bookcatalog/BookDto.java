package se.citerus.cqrs.bookstore.bookcatalog;

import se.citerus.cqrs.bookstore.TransportObject;

public class BookDto extends TransportObject {

  public String bookId;
  public String isbn;
  public String title;
  public String description;
  public double price;
  public String publisherContractId;

}
