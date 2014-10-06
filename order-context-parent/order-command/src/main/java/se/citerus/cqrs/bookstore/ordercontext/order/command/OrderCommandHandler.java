package se.citerus.cqrs.bookstore.ordercontext.order.command;

import com.google.common.eventbus.Subscribe;
import se.citerus.cqrs.bookstore.command.CommandHandler;
import se.citerus.cqrs.bookstore.domain.Repository;
import se.citerus.cqrs.bookstore.ordercontext.order.OrderLine;
import se.citerus.cqrs.bookstore.ordercontext.order.domain.Order;
import se.citerus.cqrs.bookstore.ordercontext.publishercontract.PublisherContractId;
import se.citerus.cqrs.bookstore.ordercontext.query.QueryService;

import java.util.ArrayList;
import java.util.List;

public class OrderCommandHandler implements CommandHandler {

  private final Repository repository;
  private final QueryService queryService;

  public OrderCommandHandler(Repository repository, QueryService queryService) {
    this.repository = repository;
    this.queryService = queryService;
  }

  @Subscribe
  public void handle(PlaceOrderCommand command) {
    Order order = new Order();

    List<OrderLine> orderLinesWithPublishers = new ArrayList<>();
    for (OrderLine orderLine : command.orderLines) {
      PublisherContractId publisherContractId = queryService.findPublisherContract(orderLine.productId);
      orderLinesWithPublishers.add(orderLine.withPublisher(publisherContractId));
    }
    order.place(command.orderId, command.customerInformation, orderLinesWithPublishers);
    repository.save(order);
  }

  @Subscribe
  public void handle(ActivateOrderCommand command) {
    Order order = repository.load(command.orderId, Order.class);
    order.activate();
    repository.save(order);
  }

}
