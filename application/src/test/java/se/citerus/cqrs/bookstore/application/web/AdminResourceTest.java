package se.citerus.cqrs.bookstore.application.web;

import com.sun.jersey.api.client.GenericType;
import io.dropwizard.testing.junit.ResourceTestRule;
import org.junit.After;
import org.junit.ClassRule;
import org.junit.Test;
import se.citerus.cqrs.bookstore.admin.OrderClient;
import se.citerus.cqrs.bookstore.command.CommandBus;
import se.citerus.cqrs.bookstore.event.DomainEventStore;
import se.citerus.cqrs.bookstore.order.OrderId;
import se.citerus.cqrs.bookstore.order.OrderLine;
import se.citerus.cqrs.bookstore.query.OrderProjection;
import se.citerus.cqrs.bookstore.query.QueryService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON_TYPE;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;
import static se.citerus.cqrs.bookstore.order.OrderStatus.ACTIVATED;
import static se.citerus.cqrs.bookstore.order.OrderStatus.PLACED;

public class AdminResourceTest {

  private static final String SERVICE_ADDRESS = "http://localhost:8080";
  private static final String ADMIN_RESOURCE = SERVICE_ADDRESS + "/admin";

  private static final CommandBus commandBus = mock(CommandBus.class);
  private static final DomainEventStore eventStore = mock(DomainEventStore.class);
  private static final QueryService queryService = mock(QueryService.class);
  private static final OrderClient orderClient = mock(OrderClient.class);

  public static final GenericType<List<OrderProjection>> ORDER_LIST_TYPE = new GenericType<List<OrderProjection>>() {
  };

  @ClassRule
  public static final ResourceTestRule resources = ResourceTestRule.builder()
      .addResource(new AdminResource(queryService, commandBus, eventStore, orderClient))
      .build();

  @After
  public void tearDown() throws Exception {
    reset(queryService, commandBus, eventStore);
  }

  @Test
  public void testGetOrders() {
    List<OrderProjection> orderProjections = new ArrayList<>();
    OrderProjection order1 = new OrderProjection(
        OrderId.randomId(), 0L, "TestCustomer", 200L, Collections.<OrderLine>emptyList(), PLACED);
    OrderProjection order2 = new OrderProjection(
        OrderId.randomId(), 0L, "TestCustomer", 200L, Collections.<OrderLine>emptyList(), ACTIVATED);

    orderProjections.add(order1);
    orderProjections.add(order2);

    when(queryService.listOrders()).thenReturn(orderProjections);

    List<OrderProjection> orders = resources.client()
        .resource(ADMIN_RESOURCE + "/orders")
        .accept(APPLICATION_JSON_TYPE)
        .get(ORDER_LIST_TYPE);

    Iterator<OrderProjection> ordersIterator = orders.iterator();
    assertThat(ordersIterator.next(), is(order1));
    assertThat(ordersIterator.next(), is(order2));
  }

}
