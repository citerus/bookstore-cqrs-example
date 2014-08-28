package se.citerus.cqrs.bookstore.query.repository;

import org.junit.Test;
import se.citerus.cqrs.bookstore.order.OrderId;
import se.citerus.cqrs.bookstore.order.OrderLine;
import se.citerus.cqrs.bookstore.query.OrderLineProjection;
import se.citerus.cqrs.bookstore.query.OrderProjection;

import java.util.Collections;
import java.util.Iterator;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static se.citerus.cqrs.bookstore.order.OrderStatus.PLACED;

public class InMemOrderProjectionRepositoryTest {

  @Test
  public void sortingOfOrders() {
    InMemOrderProjectionRepository repository = new InMemOrderProjectionRepository();

    repository.save(new OrderProjection(OrderId.<OrderId>randomId(), 1, "Test Person", 0, Collections.<OrderLineProjection>emptyList(), PLACED));
    repository.save(new OrderProjection(OrderId.<OrderId>randomId(), 3, "Test Person 2", 0, Collections.<OrderLineProjection>emptyList(), PLACED));

    Iterator<OrderProjection> iterator = repository.listOrdersByTimestamp().iterator();
    assertThat(iterator.next().getOrderPlacedTimestamp(), is(3L));
    assertThat(iterator.next().getOrderPlacedTimestamp(), is(1L));
  }

}
