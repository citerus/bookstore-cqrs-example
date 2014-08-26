package se.citerus.cqrs.bookstore.order.web;

import com.sun.jersey.api.client.GenericType;
import io.dropwizard.testing.junit.ResourceTestRule;
import org.junit.After;
import org.junit.ClassRule;
import org.junit.Test;
import se.citerus.cqrs.bookstore.admin.client.AdminBookClient;
import se.citerus.cqrs.bookstore.admin.client.OrderClient;
import se.citerus.cqrs.bookstore.admin.client.PublisherClient;
import se.citerus.cqrs.bookstore.admin.web.AdminResource;
import se.citerus.cqrs.bookstore.admin.web.transport.Order;
import se.citerus.cqrs.bookstore.event.DomainEventStore;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON_TYPE;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

public class AdminResourceTest {

  private static final String SERVICE_ADDRESS = "http://localhost:8080";
  private static final String ADMIN_RESOURCE = SERVICE_ADDRESS + "/admin";

  private static final DomainEventStore eventStore = mock(DomainEventStore.class);
  private static final OrderClient orderClient = mock(OrderClient.class);
  private static AdminBookClient adminBookClient = mock(AdminBookClient.class);
  private static final PublisherClient publisherClient = mock(PublisherClient.class);

  public static final GenericType<List<Order>> ORDER_LIST_TYPE = new GenericType<List<Order>>() {
  };
  @ClassRule
  public static final ResourceTestRule resources = ResourceTestRule.builder()
      .addResource(new AdminResource(orderClient, publisherClient, adminBookClient))
      .build();

  @After
  public void tearDown() throws Exception {
    reset(eventStore, adminBookClient, publisherClient, orderClient);
  }

  @Test
  public void testGetOrders() {
    List<Order> orderProjections = new ArrayList<>();
    Order order1 = new Order();
//    OrderId.randomId(), 0L, "TestCustomer", 200L, Collections.<OrderLine>emptyList(), OrderStatus.PLACED
//    OrderId.randomId(), 0L, "TestCustomer", 200L, Collections.<OrderLine>emptyList(), OrderStatus.ACTIVATED
    Order order2 = new Order();

    orderProjections.add(order1);
    orderProjections.add(order2);

    when(orderClient.listOrders()).thenReturn(orderProjections);

    List<Order> orders = resources.client()
        .resource(ADMIN_RESOURCE + "/orders")
        .accept(APPLICATION_JSON_TYPE)
        .get(ORDER_LIST_TYPE);

    Iterator<Order> ordersIterator = orders.iterator();
    assertThat(ordersIterator.next(), is(order1));
    assertThat(ordersIterator.next(), is(order2));
  }

}
