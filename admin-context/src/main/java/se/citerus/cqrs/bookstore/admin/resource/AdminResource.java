package se.citerus.cqrs.bookstore.admin.resource;

import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.citerus.cqrs.bookstore.admin.api.OrderActivationRequest;
import se.citerus.cqrs.bookstore.admin.client.order.OrderClient;
import se.citerus.cqrs.bookstore.admin.client.order.OrderDto;

import javax.validation.Valid;
import javax.ws.rs.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@Path("admin")
@Produces(APPLICATION_JSON)
@Consumes(APPLICATION_JSON)
public class AdminResource {

  private final Logger logger = LoggerFactory.getLogger(getClass());
  private final OrderClient orderClient;

  public AdminResource(OrderClient orderClient) {
    this.orderClient = orderClient;
  }

  @GET
  @Path("orders")
  public List<OrderDto> getOrders() {
    List<OrderDto> projections = orderClient.listOrders();
    logger.info("Returning [{}] orders", projections.size());
    return projections;
  }

  @GET
  @Path("events")
  public List<Object[]> getEvents() {
    List<Map<String, Object>> allEvents = orderClient.getAllEvents();
    List<Object[]> eventsToReturn = new LinkedList<>();
    for (Map event : allEvents) {
      eventsToReturn.add(new Object[]{event.get("type").toString(), event});
    }
    logger.info("Returning [{}] events", eventsToReturn.size());
    return eventsToReturn;
  }

  @POST
  @Path("order-activation-requests")
  public void orderActivationRequest(@Valid OrderActivationRequest activationRequest) {
    logger.info("Activating orderId: " + activationRequest.orderId);
    orderClient.activate(activationRequest);
  }

  // TODO: Add Simple bar chart to admin gui!
  @GET
  @Path("orders-per-day")
  public Map<LocalDate, Integer> getOrdersPerDay() {
    return orderClient.getOrdersPerDay();
  }

}
