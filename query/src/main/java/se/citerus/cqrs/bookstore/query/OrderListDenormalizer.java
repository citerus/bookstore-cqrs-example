package se.citerus.cqrs.bookstore.query;

import com.google.common.eventbus.Subscribe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.citerus.cqrs.bookstore.event.DomainEventListener;
import se.citerus.cqrs.bookstore.order.OrderId;
import se.citerus.cqrs.bookstore.order.OrderLine;
import se.citerus.cqrs.bookstore.order.event.OrderActivatedEvent;
import se.citerus.cqrs.bookstore.order.event.OrderPlacedEvent;
import se.citerus.cqrs.bookstore.query.repository.InMemOrderProjectionRepository;

import java.util.List;

import static se.citerus.cqrs.bookstore.order.OrderStatus.ACTIVATED;
import static se.citerus.cqrs.bookstore.order.OrderStatus.PLACED;

public class OrderListDenormalizer implements DomainEventListener {

  private final Logger logger = LoggerFactory.getLogger(getClass());
  private final InMemOrderProjectionRepository repository;

  public OrderListDenormalizer(InMemOrderProjectionRepository repository) {
    this.repository = repository;
  }

  @Subscribe
  public void handleEvent(OrderPlacedEvent event) {
    logger.info("Received: " + event.toString());
    List<OrderLine> orderLines = event.orderLines;
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

  public OrderProjection findById(OrderId orderId) {
    return repository.getById(orderId);
  }

}
