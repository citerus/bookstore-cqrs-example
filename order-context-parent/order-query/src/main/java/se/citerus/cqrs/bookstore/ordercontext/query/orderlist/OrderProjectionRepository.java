package se.citerus.cqrs.bookstore.ordercontext.query.orderlist;

import se.citerus.cqrs.bookstore.ordercontext.order.OrderId;

import java.util.List;

public interface OrderProjectionRepository {

  void save(OrderProjection orderProjection);

  OrderProjection getById(OrderId orderId);

  List<OrderProjection> listOrdersByTimestamp();

}
