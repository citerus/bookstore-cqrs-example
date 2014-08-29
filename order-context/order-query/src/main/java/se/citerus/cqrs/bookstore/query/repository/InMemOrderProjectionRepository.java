package se.citerus.cqrs.bookstore.query.repository;

import se.citerus.cqrs.bookstore.order.OrderId;
import se.citerus.cqrs.bookstore.query.OrderProjection;

import java.util.*;

public class InMemOrderProjectionRepository {

  private final OrderTimestampComparator orderTimestampComparator = new OrderTimestampComparator();
  private final Map<OrderId, OrderProjection> orders = new HashMap<>();

  private void saveOrder(OrderId orderId, OrderProjection orderProjection) {
    orders.put(orderId, orderProjection);
  }

  public void save(OrderProjection orderProjection) {
    saveOrder(orderProjection.getOrderId(), orderProjection);
  }

  public OrderProjection getById(OrderId orderId) {
    return orders.get(orderId);
  }

  public List<OrderProjection> listOrdersByTimestamp() {
    List<OrderProjection> projections = new ArrayList<>(orders.values());
    Collections.sort(projections, Collections.reverseOrder(orderTimestampComparator));
    return projections;
  }

  private static class OrderTimestampComparator implements Comparator<OrderProjection> {
    @Override
    public int compare(OrderProjection o1, OrderProjection o2) {
      return Long.valueOf(o1.getOrderPlacedTimestamp()).compareTo(o2.getOrderPlacedTimestamp());
    }
  }

}
