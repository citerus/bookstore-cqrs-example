package se.citerus.cqrs.bookstore.ordercontext.resource;

import se.citerus.cqrs.bookstore.event.DomainEvent;
import se.citerus.cqrs.bookstore.event.DomainEventStore;
import se.citerus.cqrs.bookstore.ordercontext.query.QueryService;
import se.citerus.cqrs.bookstore.ordercontext.query.orderlist.OrderProjection;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@Path("query")
@Produces(APPLICATION_JSON)
@Consumes(APPLICATION_JSON)
public class QueryResource {

  private final QueryService queryService;
  private final DomainEventStore eventStore;

  public QueryResource(QueryService queryService, DomainEventStore eventStore) {
    this.queryService = queryService;
    this.eventStore = eventStore;
  }

  @GET
  @Path("events")
  public List<Object[]> getAllEvents() {
    List<DomainEvent> allEvents = eventStore.getAllEvents();
    List<Object[]> eventsToReturn = new LinkedList<>();
    for (DomainEvent event : allEvents) {
      eventsToReturn.add(new Object[]{event.getClass().getSimpleName(), event});
    }
    return eventsToReturn;
  }

  @GET
  @Path("orders")
  public Collection<OrderProjection> getOrders() {
    return queryService.getOrders();
  }

  @GET
  @Path("orders-per-day")
  public Map getOrdersPerDay() {
    return queryService.getOrdersPerDay();
  }

}
