package se.citerus.cqrs.bookstore.order.web;

import se.citerus.cqrs.bookstore.query.OrderProjection;
import se.citerus.cqrs.bookstore.query.QueryService;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.Collection;

@Path("orders")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class OrderResource {

  private final QueryService queryService;

  public OrderResource(QueryService queryService) {
    this.queryService = queryService;
  }

  @GET
  public Collection<OrderProjection> getAllOrders() {
    return queryService.listOrders();
  }

}
