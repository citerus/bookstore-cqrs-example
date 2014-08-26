package se.citerus.cqrs.bookstore.query;

import com.fasterxml.jackson.annotation.JsonProperty;

import static org.apache.commons.lang3.StringUtils.isEmpty;

public class BookDto {

  public final String bookId;
  public final String isbn;
  public final String title;
  public final String description;
  public final double price;
  public final String publisherContractId;

  public BookDto(@JsonProperty("bookId") String bookId,
              @JsonProperty("isbn") String isbn,
              @JsonProperty("title") String title,
              @JsonProperty("description") String description,
              @JsonProperty("price") long price,
              @JsonProperty("publisherContractId") String publisherContractId) {
    this.bookId = bookId;
    this.isbn = isbn;
    this.title = title;
    this.description = description;
    this.price = price;
    this.publisherContractId = publisherContractId;
  }

  public boolean hasPublisher() {
    return !isEmpty(publisherContractId);
  }
}
