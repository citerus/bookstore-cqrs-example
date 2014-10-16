package se.citerus.cqrs.bookstore.ordercontext.order.command;

import com.google.common.eventbus.Subscribe;
import se.citerus.cqrs.bookstore.command.CommandHandler;
import se.citerus.cqrs.bookstore.domain.Repository;
import se.citerus.cqrs.bookstore.ordercontext.order.domain.Order;

public class OrderCommandHandler implements CommandHandler {

  private final Repository repository;

  public OrderCommandHandler(Repository repository) {
    this.repository = repository;
  }

  @Subscribe
  public void handle(PlaceOrderCommand command) {
    Order order = new Order();
    order.place(command.orderId, command.customerInformation, command.orderLines, command.totalAmount);
    repository.save(order);
  }

  @Subscribe
  public void handle(ActivateOrderCommand command) {
    Order order = repository.load(command.orderId, Order.class);
    order.activate();
    repository.save(order);
  }

}
