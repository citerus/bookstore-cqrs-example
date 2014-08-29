package se.citerus.cqrs.bookstore.admin.resource;

import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.citerus.cqrs.bookstore.admin.api.CreateBookRequest;
import se.citerus.cqrs.bookstore.admin.api.OrderActivationRequest;
import se.citerus.cqrs.bookstore.admin.api.RegisterPublisherContractRequest;
import se.citerus.cqrs.bookstore.admin.client.bookcatalog.BookCatalogClient;
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
  private final BookCatalogClient bookCatalogClient;
  private final OrderClient orderClient;

  public AdminResource(BookCatalogClient bookCatalogClient, OrderClient orderClient) {
    this.bookCatalogClient = bookCatalogClient;
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
  public List<String[]> getEvents() {
    List<Map<String, Object>> allEvents = orderClient.getAllEvents();
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
    orderClient.activate(activationRequest);
  }

  @POST
  @Path("create-book-requests")
  public void bookRequest(@Valid CreateBookRequest createBookRequest) {
    logger.info("Creating book: " + createBookRequest.bookId);
    bookCatalogClient.createBook(createBookRequest);
  }

  @POST
  @Path("publishercontract-requests")
  public void registerPublisher(@Valid RegisterPublisherContractRequest registerPublisherContractRequest) {
    logger.info("Registering publisher: " + registerPublisherContractRequest.publisherContractId);
    orderClient.registerPublisherContract(registerPublisherContractRequest);
  }

  // TODO: Add Simple bar chart to admin gui!
  @GET
  @Path("orders-per-day")
  public Map<LocalDate, Integer> getOrdersPerDay() {
    return orderClient.getOrdersPerDay();
  }

}
