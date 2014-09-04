package se.citerus.cqrs.bookstore.ordercontext.query.orderlist;

import org.junit.Before;
import org.junit.Test;
import se.citerus.cqrs.bookstore.ordercontext.infrastructure.InMemOrderProjectionRepository;
import se.citerus.cqrs.bookstore.ordercontext.order.CustomerInformation;
import se.citerus.cqrs.bookstore.ordercontext.order.OrderId;
import se.citerus.cqrs.bookstore.ordercontext.order.OrderLine;
import se.citerus.cqrs.bookstore.ordercontext.order.event.OrderActivatedEvent;
import se.citerus.cqrs.bookstore.ordercontext.order.event.OrderPlacedEvent;

import java.util.Collections;
import java.util.Iterator;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static se.citerus.cqrs.bookstore.ordercontext.order.OrderStatus.ACTIVATED;

public class OrderListDenormalizerTest {

  private OrderListDenormalizer denormalizer;

  @Before
  public void setUp() {
    denormalizer = new OrderListDenormalizer(new InMemOrderProjectionRepository());
  }

  @Test
  public void activatePlacedOrderSavesStatus() {
    OrderId orderId = OrderId.randomId();
    CustomerInformation customerInformation = new CustomerInformation("name", "someone@acme.com", "address");

    OrderPlacedEvent event1 = new OrderPlacedEvent(orderId, 0, 1, customerInformation,
        Collections.<OrderLine>emptyList(), 0);
    denormalizer.handleEvent(event1);

    OrderActivatedEvent event2 = new OrderActivatedEvent(orderId, 1, 2);
    denormalizer.handleEvent(event2);

    Iterator<OrderProjection> iterator = denormalizer.getOrders().iterator();
    OrderProjection order = iterator.next();
    assertThat(order.getOrderPlacedTimestamp(), is(1L));
    assertThat(order.getStatus(), is(ACTIVATED));
  }

}
