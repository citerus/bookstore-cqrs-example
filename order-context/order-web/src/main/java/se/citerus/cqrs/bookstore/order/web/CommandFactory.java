package se.citerus.cqrs.bookstore.order.web;

import se.citerus.cqrs.bookstore.order.BookId;
import se.citerus.cqrs.bookstore.order.CustomerInformation;
import se.citerus.cqrs.bookstore.order.OrderId;
import se.citerus.cqrs.bookstore.order.OrderLine;
import se.citerus.cqrs.bookstore.order.command.ActivateOrderCommand;
import se.citerus.cqrs.bookstore.order.command.PlaceOrderCommand;
import se.citerus.cqrs.bookstore.order.publisher.command.RegisterPublisherContractCommand;
import se.citerus.cqrs.bookstore.order.web.transport.CartDto;
import se.citerus.cqrs.bookstore.order.web.transport.LineItemDto;
import se.citerus.cqrs.bookstore.order.web.transport.PlaceOrderRequest;
import se.citerus.cqrs.bookstore.publisher.PublisherContractId;

import java.util.ArrayList;
import java.util.List;

public class CommandFactory {

  public RegisterPublisherContractCommand toCommand(PublisherContractId publisherContractId, RegisterPublisherRequest request) {
    return new RegisterPublisherContractCommand(publisherContractId, request.publisherName, request.feePercentage, request.limit);
  }
  public PlaceOrderCommand toCommand(CartDto cart, PlaceOrderRequest request) {
    List<OrderLine> itemsToOrder = getOrderLines(cart);
    CustomerInformation customerInformation = getCustomerInformation(request);
    return new PlaceOrderCommand(new OrderId(request.orderId), customerInformation, itemsToOrder);
  }

  public ActivateOrderCommand toCommand(OrderActivationRequest request) {
    return new ActivateOrderCommand(new OrderId(request.orderId));
  }

  private List<OrderLine> getOrderLines(CartDto cart) {
    List<OrderLine> itemsToOrder = new ArrayList<>();
    for (LineItemDto lineItem : cart.lineItems) {
      BookId bookId = new BookId(lineItem.bookId);
      String title = lineItem.title;
      int quantity = lineItem.quantity;
      long price = lineItem.price;
      itemsToOrder.add(new OrderLine(bookId, title, quantity, price));
    }
    return itemsToOrder;
  }

  private CustomerInformation getCustomerInformation(PlaceOrderRequest request) {
    return new CustomerInformation(request.customerName,
        request.customerEmail, request.customerAddress);
  }

}
