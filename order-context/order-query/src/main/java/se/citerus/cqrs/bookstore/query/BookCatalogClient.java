package se.citerus.cqrs.bookstore.query;

import com.sun.jersey.api.client.Client;

import javax.ws.rs.core.MediaType;

public class BookCatalogClient {

  private final Client client;

  private BookCatalogClient(Client client) {
    this.client = client;
  }

  public static BookCatalogClient create(Client client) {
    return new BookCatalogClient(client);
  }

  public BookDto getBook(String bookId) {
    return client.resource("http://localhost:8080/service/books/" + bookId)
        .accept(MediaType.APPLICATION_JSON_TYPE).get(BookDto.class);
  }


}
