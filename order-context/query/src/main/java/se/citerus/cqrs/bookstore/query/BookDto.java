package se.citerus.cqrs.bookstore.query;

import static org.apache.commons.lang3.StringUtils.isEmpty;

public class BookDto {

  public String bookId;
  public String isbn;
  public String title;
  public String description;
  public double price;
  public String publisherContractId;

  public boolean hasPublisher() {
    return !isEmpty(publisherContractId);
  }
}
