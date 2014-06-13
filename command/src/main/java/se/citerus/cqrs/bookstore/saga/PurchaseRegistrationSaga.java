package se.citerus.cqrs.bookstore.saga;

import com.google.common.eventbus.Subscribe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.citerus.cqrs.bookstore.command.CommandBus;
import se.citerus.cqrs.bookstore.command.publisher.RegisterPurchaseCommand;
import se.citerus.cqrs.bookstore.order.OrderLine;
import se.citerus.cqrs.bookstore.order.event.OrderActivatedEvent;
import se.citerus.cqrs.bookstore.query.OrderProjection;
import se.citerus.cqrs.bookstore.query.QueryService;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PurchaseRegistrationSaga extends Saga {

  private final Logger logger = LoggerFactory.getLogger(getClass());
  private final QueryService queryService;
  private final CommandBus commandBus;
  private final ExecutorService executorService = Executors.newFixedThreadPool(1);

  public PurchaseRegistrationSaga(QueryService queryService, CommandBus commandBus) {
    this.queryService = queryService;
    this.commandBus = commandBus;
  }

  @Subscribe
  public void handleEvent(OrderActivatedEvent event) {
    logger.info("Received: " + event.toString());
    OrderProjection order = queryService.getOrder(event.aggregateId);
    for (final OrderLine orderLine : order.getOrderLines()) {
      if (orderLine.bookHasRegisteredPublisher()) {
        publish(new RegisterPurchaseCommand(orderLine.publisherId, orderLine.bookId, orderLine.amount()));
      }
    }
  }

  private void publish(final RegisterPurchaseCommand command) {
    executorService.submit(new Runnable() {
      @Override
      public void run() {
        commandBus.dispatch(command);
      }
    });
  }

}
