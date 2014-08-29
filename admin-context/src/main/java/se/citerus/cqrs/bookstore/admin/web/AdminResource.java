package se.citerus.cqrs.bookstore.admin.web;

import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.citerus.cqrs.bookstore.admin.client.AdminClient;
import se.citerus.cqrs.bookstore.admin.web.request.CreateBookRequest;
import se.citerus.cqrs.bookstore.admin.web.request.OrderActivationRequest;
import se.citerus.cqrs.bookstore.admin.web.request.RegisterPublisherRequest;
import se.citerus.cqrs.bookstore.admin.web.transport.OrderDto;

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
  private final AdminClient adminClient;

  public AdminResource(AdminClient adminClient) {
    this.adminClient = adminClient;
  }

  @GET
  @Path("orders")
  public List<OrderDto> getOrders() {
    List<OrderDto> projections = adminClient.listOrders();
    logger.info("Returning [{}] orders", projections.size());
    return projections;
  }

  @GET
  @Path("events")
  public List<String[]> getEvents() {
    List<Map<String, Object>> allEvents = adminClient.getAllEvents();
    List<String[]> eventsToReturn = new LinkedList<>();
    for (Map event : allEvents) {
      eventsToReturn.add(new String[]{event.get("type").toString(), event.toString()});
    }
    logger.info("Returning [{}] events", eventsToReturn.size());
    return eventsToReturn;
  }

  @POST
  @Path("order-activation-requests")
  public void orderActivationRequest(@Valid OrderActivationRequest activationRequest) {
    logger.info("Activating orderId: " + activationRequest.orderId);
    adminClient.activate(activationRequest);
  }

  @POST
  @Path("create-book-requests")
  public void bookRequest(@Valid CreateBookRequest createBookRequest) {
    logger.info("Creating book: " + createBookRequest.bookId);
    adminClient.createBook(createBookRequest);
  }

  @POST
  @Path("register-publisher-requests")
  public void registerPublisher(@Valid RegisterPublisherRequest registerPublisherRequest) {
    logger.info("Registering publisher: " + registerPublisherRequest.publisherContractId);
    adminClient.registerPublisher(registerPublisherRequest);
  }

  // TODO: Add Simple bar chart to admin gui!
  @GET
  @Path("orders-per-day")
  public Map<LocalDate, Integer> getOrdersPerDay() {
    return adminClient.getOrdersPerDay();
  }

}
