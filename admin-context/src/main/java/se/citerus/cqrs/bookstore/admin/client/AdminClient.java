package se.citerus.cqrs.bookstore.admin.client;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.GenericType;
import org.joda.time.LocalDate;
import se.citerus.cqrs.bookstore.admin.web.request.CreateBookRequest;
import se.citerus.cqrs.bookstore.admin.web.request.OrderActivationRequest;
import se.citerus.cqrs.bookstore.admin.web.request.RegisterPublisherContractRequest;
import se.citerus.cqrs.bookstore.admin.web.transport.OrderDto;

import java.util.List;
import java.util.Map;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON_TYPE;

public class AdminClient {

  public static final GenericType<List<OrderDto>> ORDER_LIST_TYPE = new GenericType<List<OrderDto>>() {
  };

  public static final GenericType<List<Map<String, Object>>> EVENT_LIST_TYPE = new GenericType<List<Map<String, Object>>>() {
  };

  private final Client client;

  private AdminClient(Client client) {
    this.client = client;
  }

  public static AdminClient create(Client client) {
    return new AdminClient(client);
  }

  public void createBook(CreateBookRequest createBookRequest) {
    client.resource("http://localhost:8080/service/books")
        .entity(createBookRequest, APPLICATION_JSON_TYPE).post();
  }

  public void activate(OrderActivationRequest activationRequest) {
    client.resource("http://localhost:8080/service/order-requests/activations")
        .entity(activationRequest, APPLICATION_JSON_TYPE).post();
  }

  public void registerPublisherContract(RegisterPublisherContractRequest registerPublisherContractRequest) {
    client.resource("http://localhost:8080/service/register-publisher-requests/register")
        .entity(registerPublisherContractRequest, APPLICATION_JSON_TYPE).post();
  }

  public List<OrderDto> listOrders() {
    return client.resource("http://localhost:8080/service/orders")
        .accept(APPLICATION_JSON_TYPE).get(ORDER_LIST_TYPE);
  }

  public List<Map<String, Object>> getAllEvents() {
    return client.resource("http://localhost:8080/service/orders/events")
        .accept(APPLICATION_JSON_TYPE).get(EVENT_LIST_TYPE);
  }

  public Map<LocalDate, Integer> getOrdersPerDay() {
    return null;
  }

}
