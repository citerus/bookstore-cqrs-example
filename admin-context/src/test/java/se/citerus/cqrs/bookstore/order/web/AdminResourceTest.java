package se.citerus.cqrs.bookstore.order.web;

import com.sun.jersey.api.client.GenericType;
import io.dropwizard.testing.junit.ResourceTestRule;
import org.junit.After;
import org.junit.ClassRule;
import org.junit.Test;
import se.citerus.cqrs.bookstore.admin.web.client.bookcatalog.BookCatalogClient;
import se.citerus.cqrs.bookstore.admin.web.client.order.IdDto;
import se.citerus.cqrs.bookstore.admin.web.client.order.OrderClient;
import se.citerus.cqrs.bookstore.admin.web.client.order.OrderDto;
import se.citerus.cqrs.bookstore.admin.web.client.order.OrderLineDto;
import se.citerus.cqrs.bookstore.admin.web.resource.AdminResource;
import se.citerus.cqrs.bookstore.event.DomainEventStore;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON_TYPE;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

public class AdminResourceTest {

  private static final String ADMIN_RESOURCE = "/admin";

  private static final DomainEventStore eventStore = mock(DomainEventStore.class);
  private static final BookCatalogClient BOOK_CATALOG_CLIENT = mock(BookCatalogClient.class);
  private static final OrderClient ORDER_CLIENT = mock(OrderClient.class);

  public static final GenericType<List<OrderDto>> ORDER_LIST_TYPE = new GenericType<List<OrderDto>>() {
  };

  @ClassRule
  public static final ResourceTestRule resources = ResourceTestRule.builder()
      .addResource(new AdminResource(BOOK_CATALOG_CLIENT, ORDER_CLIENT))
      .build();

  @After
  public void tearDown() throws Exception {
    reset(eventStore, BOOK_CATALOG_CLIENT, ORDER_CLIENT);
  }

  @Test
  public void testGetOrders() {
    List<OrderDto> orderProjections = new ArrayList<>();
    OrderDto order1 = new OrderDto();
    order1.orderId = randomId();
    order1.customerName = "TestCustomer";
    order1.orderAmount = 200L;
    order1.orderPlacedTimestamp = System.currentTimeMillis();
    OrderLineDto orderLine = new OrderLineDto();
    orderLine.bookId = randomId();
    orderLine.publisherContractId = randomId();
    orderLine.quantity = 10;
    orderLine.unitPrice = 10;
    order1.orderLines = asList(orderLine);
    order1.status = "PLACED";
    OrderDto order2 = new OrderDto();
    order2.orderId = randomId();
    order2.orderAmount = 200L;
    order2.orderLines = emptyList();
    order1.status = "ACTIVATED";

    orderProjections.add(order1);
    orderProjections.add(order2);

    when(ORDER_CLIENT.listOrders()).thenReturn(orderProjections);

    List<OrderDto> orders = resources.client()
        .resource(ADMIN_RESOURCE + "/orders")
        .accept(APPLICATION_JSON_TYPE)
        .get(ORDER_LIST_TYPE);

    Iterator<OrderDto> ordersIterator = orders.iterator();
    assertThat(ordersIterator.next(), is(order1));
    assertThat(ordersIterator.next(), is(order2));
  }

  private static IdDto randomId() {
    IdDto idDto = new IdDto();
    idDto.id = UUID.randomUUID().toString();
    return idDto;
  }

}
