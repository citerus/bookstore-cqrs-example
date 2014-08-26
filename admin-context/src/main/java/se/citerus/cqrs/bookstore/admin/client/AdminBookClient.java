package se.citerus.cqrs.bookstore.admin.client;

import com.sun.jersey.api.client.Client;
import se.citerus.cqrs.bookstore.admin.web.transport.CreateBookRequest;
import se.citerus.cqrs.bookstore.admin.web.transport.RegisterPublisherRequest;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON_TYPE;

public class AdminBookClient {

  private final Client client;

  private AdminBookClient(Client client) {
    this.client = client;
  }

  public static AdminBookClient create(Client client) {
    return new AdminBookClient(client);
  }

  public void registerPublisher(RegisterPublisherRequest registerPublisherRequest) {
    client.resource("http://localhost:8080/order-requests/activations")
        .entity(registerPublisherRequest, APPLICATION_JSON_TYPE).post();
  }


  public void createBook(CreateBookRequest createBookRequest) {
    client.resource("http://localhost:8080/books")
        .entity(createBookRequest, APPLICATION_JSON_TYPE).post();
  }
}
