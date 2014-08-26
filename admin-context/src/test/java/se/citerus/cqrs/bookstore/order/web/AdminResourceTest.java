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
import se.citerus.cqrs.bookstore.admin.web.transport.IdDto;
import se.citerus.cqrs.bookstore.admin.web.transport.OrderDto;
import se.citerus.cqrs.bookstore.admin.web.transport.OrderLineDto;
import se.citerus.cqrs.bookstore.event.DomainEventStore;

import java.util.*;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
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

  public static final GenericType<List<OrderDto>> ORDER_LIST_TYPE = new GenericType<List<OrderDto>>() {
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
    List<OrderDto> orderProjections = new ArrayList<>();
    OrderDto order1 = new OrderDto();
    order1.orderId = IdDto.random();
    order1.customerName = "TestCustomer";
    order1.orderAmount = 200L;
    OrderLineDto orderLine = new OrderLineDto();
    orderLine.bookId = IdDto.random();
    orderLine.publisherContractId = IdDto.random();
    orderLine.quantity = 10;
    orderLine.unitPrice = 10;
    order1.orderLines = asList(orderLine);
    order1.status = "PLACED";
    OrderDto order2 = new OrderDto();
    order2.orderId = IdDto.random();
    order2.orderAmount = 200L;
    order2.orderLines = emptyList();
    order1.status = "ACTIVATED";

    orderProjections.add(order1);
    orderProjections.add(order2);

    when(orderClient.listOrders()).thenReturn(orderProjections);

    List<OrderDto> orders = resources.client()
        .resource(ADMIN_RESOURCE + "/orders")
        .accept(APPLICATION_JSON_TYPE)
        .get(ORDER_LIST_TYPE);

    Iterator<OrderDto> ordersIterator = orders.iterator();
    assertThat(ordersIterator.next(), is(order1));
    assertThat(ordersIterator.next(), is(order2));
  }

}
