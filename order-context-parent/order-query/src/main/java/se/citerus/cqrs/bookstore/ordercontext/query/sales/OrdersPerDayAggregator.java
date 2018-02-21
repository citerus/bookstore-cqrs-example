package se.citerus.cqrs.bookstore.ordercontext.query.sales;

import com.google.common.eventbus.Subscribe;
import org.joda.time.LocalDate;
import se.citerus.cqrs.bookstore.event.DomainEventListener;
import se.citerus.cqrs.bookstore.ordercontext.order.event.OrderPlacedEvent;

import java.util.Map;
import java.util.TreeMap;

/**
 * Simple in memory aggregator counting placed orders per day based on order timestamp.
 */
public class OrdersPerDayAggregator implements DomainEventListener {

  private final TreeMap<LocalDate, Integer> orders = new TreeMap<>();

  @Subscribe
  public void handleEvent(OrderPlacedEvent event) {
    LocalDate localDate = new LocalDate(event.timestamp);
    Integer ordersPerDay = orders.get(localDate);
    orders.put(localDate, nullSafeIncrease(ordersPerDay));
  }

  private Integer nullSafeIncrease(Integer integer) {
    return integer == null ? 1 : integer + 1;
  }

  @Override
  public boolean supportsReplay() {
    return true;
  }

  public Map<LocalDate, Integer> getOrdersPerDay() {
    return orders;
  }

}
