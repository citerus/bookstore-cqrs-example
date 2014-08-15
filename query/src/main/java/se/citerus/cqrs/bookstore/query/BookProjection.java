package se.citerus.cqrs.bookstore.query;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BookProjection extends Projection {

  private String bookId;
  private String isbn;
  private String title;
  private String description;
  private long price;
  private String publisherContractId;

  public BookProjection(@JsonProperty("bookId") String bookId,
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

  public String getIsbn() {
    return isbn;
  }

  public String getTitle() {
    return title;
  }

  public String getPublisherContractId() {
    return publisherContractId;
  }

  public String getDescription() {
    return description;
  }

  public long getPrice() {
    return price;
  }

  public void setPrice(long price) {
    this.price = price;
  }

  public String getBookId() {
    return bookId;
  }

  public boolean hasPublisher() {
    return publisherContractId != null;
  }

}
