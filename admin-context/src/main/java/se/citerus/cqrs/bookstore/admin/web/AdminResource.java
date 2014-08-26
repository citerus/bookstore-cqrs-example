package se.citerus.cqrs.bookstore.admin.web;

import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.citerus.cqrs.bookstore.admin.client.AdminBookClient;
import se.citerus.cqrs.bookstore.admin.client.OrderClient;
import se.citerus.cqrs.bookstore.admin.client.PublisherClient;
import se.citerus.cqrs.bookstore.admin.web.transport.CreateBookRequest;
import se.citerus.cqrs.bookstore.admin.web.transport.OrderDto;
import se.citerus.cqrs.bookstore.admin.web.transport.OrderActivationRequest;
import se.citerus.cqrs.bookstore.admin.web.transport.RegisterPublisherRequest;

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
  private final PublisherClient publisherClient;
  private final AdminBookClient bookClient;

  public AdminResource(OrderClient orderClient, PublisherClient publisherClient, AdminBookClient bookClient) {
    this.orderClient = orderClient;
    this.publisherClient = publisherClient;
    this.bookClient = bookClient;
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
  public List<String[]> getEvents() {
    List<Map<String, Object>> allEvents = orderClient.getAllEvents();
    List<String[]> eventsToReturn = new LinkedList<>();
    for (Map event : allEvents) {
      eventsToReturn.add(new String[]{event.getClass().getSimpleName(), event.toString()});
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

  @POST
  @Path("create-book-requests")
  public void bookRequest(@Valid CreateBookRequest createBookRequest) {
    logger.info("Creating book: " + createBookRequest.bookId);
    bookClient.createBook(createBookRequest);
  }

  @POST
  @Path("register-publisher-requests")
  public void registerPublisher(@Valid RegisterPublisherRequest registerPublisherRequest) {
    logger.info("Registering publisher: " + registerPublisherRequest.publisherContractId);
    publisherClient.registerPublisher(registerPublisherRequest);
  }

  // TODO: Add Simple bar chart to admin gui!
  @GET
  @Path("orders-per-day")
  public Map<LocalDate, Integer> getOrdersPerDay() {
    return orderClient.getOrdersPerDay();
  }

}
