package se.citerus.cqrs.bookstore.ordercontext.infrastructure;

import se.citerus.cqrs.bookstore.ordercontext.order.OrderId;
import se.citerus.cqrs.bookstore.ordercontext.query.orderlist.OrderProjection;
import se.citerus.cqrs.bookstore.ordercontext.query.orderlist.OrderProjectionRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemOrderProjectionRepository implements OrderProjectionRepository {

  private final OrderTimestampComparator orderTimestampComparator = new OrderTimestampComparator();
  private final Map<OrderId, OrderProjection> orders = new HashMap<>();

  private void saveOrder(OrderId orderId, OrderProjection orderProjection) {
    orders.put(orderId, orderProjection);
  }

  @Override
  public void save(OrderProjection orderProjection) {
    saveOrder(orderProjection.getOrderId(), orderProjection);
  }

  @Override
  public OrderProjection getById(OrderId orderId) {
    return orders.get(orderId);
  }

  @Override
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
