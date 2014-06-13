package se.citerus.cqrs.bookstore.application.web;

import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.citerus.cqrs.bookstore.application.CommandFactory;
import se.citerus.cqrs.bookstore.application.web.transport.*;
import se.citerus.cqrs.bookstore.book.BookId;
import se.citerus.cqrs.bookstore.command.CommandBus;
import se.citerus.cqrs.bookstore.command.book.CreateBookCommand;
import se.citerus.cqrs.bookstore.command.book.UpdateBookPriceCommand;
import se.citerus.cqrs.bookstore.command.order.ActivateOrderCommand;
import se.citerus.cqrs.bookstore.command.publisher.RegisterPublisherCommand;
import se.citerus.cqrs.bookstore.command.publisher.UpdatePublisherFeeCommand;
import se.citerus.cqrs.bookstore.event.DomainEvent;
import se.citerus.cqrs.bookstore.event.DomainEventStore;
import se.citerus.cqrs.bookstore.order.OrderId;
import se.citerus.cqrs.bookstore.publisher.PublisherId;
import se.citerus.cqrs.bookstore.query.OrderProjection;
import se.citerus.cqrs.bookstore.query.PublisherProjection;
import se.citerus.cqrs.bookstore.query.QueryService;

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
  private final QueryService queryService;
  private final CommandBus commandBus;
  private final CommandFactory commandFactory = new CommandFactory();
  private final DomainEventStore eventStore;

  public AdminResource(QueryService queryService, CommandBus commandBus, DomainEventStore eventStore) {
    this.queryService = queryService;
    this.commandBus = commandBus;
    this.eventStore = eventStore;
  }

  @GET
  @Path("orders")
  public List<OrderProjection> getOrders() {
    List<OrderProjection> projections = queryService.listOrders();
    logger.info("Returning [{}] orders", projections.size());
    return projections;
  }

  @GET
  @Path("orders/{orderId}")
  public OrderProjection getOrder(@PathParam("orderId") OrderId orderId) {
    OrderProjection order = queryService.getById(orderId);
    logger.info("Returning order: " + order);
    return order;
  }

  @GET
  @Path("events")
  public List<String[]> getEvents() {
    List<se.citerus.cqrs.bookstore.event.DomainEvent> allEvents = eventStore.getAllEvents();
    List<String[]> eventsToReturn = new LinkedList<>();
    for (DomainEvent event : allEvents) {
      eventsToReturn.add(new String[]{event.getClass().getSimpleName(), event.toString()});
    }
    logger.info("Returning [{}] events", eventsToReturn.size());
    return eventsToReturn;
  }

  @POST
  @Path("order-activation-requests")
  public void orderActivationRequest(@Valid OrderActivationRequest activationRequest) {
    logger.info("Activating orderId: " + activationRequest.orderId);
    ActivateOrderCommand command = commandFactory.toCommand(activationRequest);
    commandBus.dispatch(command);
  }

  // TODO: Remove unused use case?
  @POST
  @Path("update-book-price-requests")
  public void updateBookPrice(@Valid UpdateBookPriceRequest updateBookPriceRequest) {
    logger.info("Updating price for book: " + updateBookPriceRequest.bookId);
    UpdateBookPriceCommand command = commandFactory.toCommand(updateBookPriceRequest);
    commandBus.dispatch(command);
  }

  @POST
  @Path("create-book-requests")
  public void bookRequest(@Valid CreateBookRequest createBookRequest) {
    BookId bookId = new BookId(createBookRequest.bookId);
    logger.info("Creating book: " + bookId);
    CreateBookCommand command = commandFactory.toCommand(bookId, createBookRequest);
    commandBus.dispatch(command);
  }

  @POST
  @Path("register-publisher-requests")
  public void registerPublisher(@Valid RegisterPublisherRequest registerPublisherRequest) {
    PublisherId publisherId = new PublisherId(registerPublisherRequest.publisherId);
    logger.info("Registering publisher: " + publisherId);
    RegisterPublisherCommand command = commandFactory.toCommand(publisherId, registerPublisherRequest);
    commandBus.dispatch(command);
  }

  // TODO: Remove unused use case?
  @POST
  @Path("update-publisher-fee-requests")
  public void updatePublisherFee(@Valid UpdatePublisherFeeRequest updatePublisherFeeRequest) {
    PublisherId publisherId = new PublisherId(updatePublisherFeeRequest.publisherId);
    logger.info("Updating fee for publisher: " + publisherId);
    UpdatePublisherFeeCommand command = commandFactory.toCommand(updatePublisherFeeRequest);
    commandBus.dispatch(command);
  }

  @GET
  @Path("publishers/{publisherId}")
  public PublisherProjection getPublisher(@PathParam("publisherId") String publisherId) {
    PublisherId publisherId1 = new PublisherId(publisherId);
    return queryService.findPublisherById(publisherId1);
  }

  @GET
  @Path("orders-per-day")
  public Map<LocalDate, Integer> getOrdersPerDay() {
    return queryService.getOrdersPerDay();
  }

}
