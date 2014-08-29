package se.citerus.cqrs.bookstore.query.orderlist;

import com.google.common.eventbus.Subscribe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.citerus.cqrs.bookstore.event.DomainEventListener;
import se.citerus.cqrs.bookstore.ordercontext.order.OrderId;
import se.citerus.cqrs.bookstore.ordercontext.order.OrderLine;
import se.citerus.cqrs.bookstore.ordercontext.order.event.OrderActivatedEvent;
import se.citerus.cqrs.bookstore.ordercontext.order.event.OrderPlacedEvent;
import se.citerus.cqrs.bookstore.query.repository.InMemOrderProjectionRepository;

import java.util.LinkedList;
import java.util.List;

import static se.citerus.cqrs.bookstore.ordercontext.order.OrderStatus.ACTIVATED;
import static se.citerus.cqrs.bookstore.ordercontext.order.OrderStatus.PLACED;

public class OrderListDenormalizer implements DomainEventListener {

  private final Logger logger = LoggerFactory.getLogger(getClass());
  private final InMemOrderProjectionRepository repository;

  public OrderListDenormalizer(InMemOrderProjectionRepository repository) {
    this.repository = repository;
  }

  @Subscribe
  public void handleEvent(OrderPlacedEvent event) {
    logger.info("Received: " + event.toString());
    List<OrderLineProjection> orderLines = new LinkedList<>();
    for (OrderLine orderLine : event.orderLines) {
      OrderLineProjection line = new OrderLineProjection();
      line.bookId = orderLine.bookId;
      line.quantity = orderLine.quantity;
      line.title = orderLine.title;
      line.unitPrice = orderLine.unitPrice;
      line.publisherContractId = orderLine.publisherContractId;
      orderLines.add(line);
    }
    OrderProjection orderProjection = new OrderProjection(event.aggregateId, event.timestamp,
        event.customerInformation.customerName, event.orderAmount, orderLines, PLACED);
    repository.save(orderProjection);
  }

  @Subscribe
  public void handleEvent(OrderActivatedEvent event) {
    logger.info("Received: " + event.toString());
    OrderProjection orderProjection = repository.getById(event.aggregateId);
    orderProjection.setStatus(ACTIVATED);
  }

  public List<OrderProjection> listOrders() {
    return repository.listOrdersByTimestamp();
  }

  @Override
  public boolean supportsReplay() {
    return true;
  }

  public OrderProjection get(OrderId orderId) {
    return repository.getById(orderId);
  }

}
