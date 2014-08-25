package se.citerus.cqrs.bookstore.application.web;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import se.citerus.cqrs.bookstore.shopping.web.transport.CartDto;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON_TYPE;

public class CartClient {

  private final Client client;

  private CartClient(Client client) {
    this.client = client;
  }

  public static CartClient create(Client client) {
    return new CartClient(client);
  }

  public CartDto get(String cartId) {
    return client.resource("http://localhost:8080/carts/" + cartId).accept(APPLICATION_JSON_TYPE).get(CartDto.class);
  }

  public void delete(String cartId) {
    client.resource("http://localhost:8080/carts/" + cartId).delete(ClientResponse.class);
  }
}
