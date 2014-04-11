package se.citerus.cqrs.bookstore.domain.order;

import org.junit.Test;
import se.citerus.cqrs.bookstore.book.BookId;
import se.citerus.cqrs.bookstore.event.DomainEvent;
import se.citerus.cqrs.bookstore.order.CustomerInformation;
import se.citerus.cqrs.bookstore.order.OrderId;
import se.citerus.cqrs.bookstore.order.OrderLine;

import java.util.Collections;
import java.util.List;

import static com.google.common.collect.Iterables.size;
import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static se.citerus.cqrs.bookstore.order.OrderStatus.ACTIVATED;
import static se.citerus.cqrs.bookstore.order.OrderStatus.PLACED;

public class OrderTest {

  private static final CustomerInformation JOHN_DOE = new CustomerInformation("John Doe", "john@acme.com", "Highway street 1");

  @Test
  public void placingAnOrder() {
    Order order = new Order();
    OrderLine orderLine = new OrderLine(BookId.<BookId>randomId(), "title", 10, 200L, null);
    order.place(OrderId.<OrderId>randomId(), JOHN_DOE, asList(orderLine));

    assertThat(order.status(), is(PLACED));
  }

  @Test
  public void activatingAnOrderDoesNotUpdateTheItemPriceIfItHasBeenRaised() {
    long originalPrice = 200L;
    OrderLine orderLine = new OrderLine(BookId.<BookId>randomId(), "title", 10, originalPrice, null);

    Order order = new Order();
    order.place(OrderId.<OrderId>randomId(), JOHN_DOE, asList(orderLine));

    order.markChangesAsCommitted();

    order.activate();

    List<DomainEvent> uncommittedEvents = order.getUncommittedEvents();
    assertThat(uncommittedEvents.size(), is(1));

    List<OrderLine> orderLines = order.orderLines();
    assertThat(size(orderLines), is(1));
    assertThat(order.status(), is(ACTIVATED));
    assertThat(orderLines.iterator().next().originalPrice, is(originalPrice));
  }

  @Test(expected = IllegalStateException.class)
  public void cannotPlaceAnOrderTwice() {
    Order order = new Order();
    order.place(OrderId.<OrderId>randomId(), JOHN_DOE, Collections.<OrderLine>emptyList());
    order.place(OrderId.<OrderId>randomId(), JOHN_DOE, Collections.<OrderLine>emptyList());
  }

  @Test
  public void activatingTwiceDoesNotGenerateASecondActivationEvent() {
    Order order = new Order();
    BookId bookId = BookId.randomId();
    long originalPrice = 200L;
    OrderLine orderLine = new OrderLine(bookId, "title", 10, originalPrice, null);
    order.place(OrderId.<OrderId>randomId(), JOHN_DOE, asList(orderLine));

    order.markChangesAsCommitted();

    order.activate();
    assertThat(order.getUncommittedEvents().size(), is(1));

    order.markChangesAsCommitted();
    order.activate();
    assertThat(order.getUncommittedEvents().size(), is(0));
  }

}
