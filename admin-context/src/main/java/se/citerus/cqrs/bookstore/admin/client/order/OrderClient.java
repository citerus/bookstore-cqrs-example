package se.citerus.cqrs.bookstore.admin.client.order;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.GenericType;
import org.joda.time.LocalDate;
import se.citerus.cqrs.bookstore.admin.api.OrderActivationRequest;

import java.util.List;
import java.util.Map;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON_TYPE;

public class OrderClient {

  public static final GenericType<List<OrderDto>> ORDER_LIST_TYPE = new GenericType<List<OrderDto>>() {
  };

  public static final GenericType<List<Object[]>> EVENT_LIST_TYPE = new GenericType<List<Object[]>>() {
  };

  private final Client client;

  private OrderClient(Client client) {
    this.client = client;
  }

  public static OrderClient create(Client client) {
    return new OrderClient(client);
  }

  public void activate(OrderActivationRequest activationRequest) {
    client.resource("http://localhost:8080/service/order-requests/activations")
        .entity(activationRequest, APPLICATION_JSON_TYPE).post();
  }

  public List<OrderDto> listOrders() {
    return client.resource("http://localhost:8080/service/query/orders")
        .accept(APPLICATION_JSON_TYPE).get(ORDER_LIST_TYPE);
  }

  public List<Object[]> getAllEvents() {
    return client.resource("http://localhost:8080/service/query/events")
        .accept(APPLICATION_JSON_TYPE).get(EVENT_LIST_TYPE);
  }

  public Map<LocalDate, Integer> getOrdersPerDay() {
    return null;
  }

}
