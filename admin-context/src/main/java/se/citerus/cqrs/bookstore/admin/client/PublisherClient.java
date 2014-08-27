package se.citerus.cqrs.bookstore.admin.client;

import com.sun.jersey.api.client.Client;
import se.citerus.cqrs.bookstore.admin.web.transport.RegisterPublisherRequest;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON_TYPE;

public class PublisherClient {
  private final Client client;

  private PublisherClient(Client client) {
    this.client = client;
  }

  public static PublisherClient create(Client client) {
    return new PublisherClient(client);
  }

  public void registerPublisher(RegisterPublisherRequest registerPublisherRequest) {
    client.resource("http://localhost:8080/publisher-requests/register")
        .entity(registerPublisherRequest, APPLICATION_JSON_TYPE).post();
  }

}
