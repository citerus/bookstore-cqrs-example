package se.citerus.cqrs.bookstore.admin.client.bookcatalog;

import com.sun.jersey.api.client.Client;
import se.citerus.cqrs.bookstore.admin.api.CreateBookRequest;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON_TYPE;

public class BookCatalogClient {

  private final Client client;

  private BookCatalogClient(Client client) {
    this.client = client;
  }

  public static BookCatalogClient create(Client client) {
    return new BookCatalogClient(client);
  }

  public void createBook(CreateBookRequest createBookRequest) {
    client.resource("http://localhost:8080/service/books")
        .entity(createBookRequest, APPLICATION_JSON_TYPE).post();
  }

}
