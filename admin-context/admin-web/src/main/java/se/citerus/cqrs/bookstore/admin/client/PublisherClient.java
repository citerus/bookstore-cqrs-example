package se.citerus.cqrs.bookstore.admin.client;

import com.sun.jersey.api.client.Client;
import se.citerus.cqrs.bookstore.admin.web.transport.OrderActivationRequest;
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

  public void activate(OrderActivationRequest activationRequest) {
    client.resource("http://localhost:8080/order-requests/activations")
        .entity(activationRequest, APPLICATION_JSON_TYPE).post();
  }

  public void registerPublisher(RegisterPublisherRequest registerPublisherRequest) {
    client.resource("http://localhost:8080/order-requests/activations")
        .entity(registerPublisherRequest, APPLICATION_JSON_TYPE).post();
  }

}
