package se.citerus.cqrs.bookstore.ordercontext.order.command;

import se.citerus.cqrs.bookstore.ordercontext.api.CartDto;
import se.citerus.cqrs.bookstore.ordercontext.api.LineItemDto;
import se.citerus.cqrs.bookstore.ordercontext.api.OrderActivationRequest;
import se.citerus.cqrs.bookstore.ordercontext.api.PlaceOrderRequest;
import se.citerus.cqrs.bookstore.ordercontext.order.BookId;
import se.citerus.cqrs.bookstore.ordercontext.order.CustomerInformation;
import se.citerus.cqrs.bookstore.ordercontext.order.OrderId;
import se.citerus.cqrs.bookstore.ordercontext.order.OrderLine;

import java.util.ArrayList;
import java.util.List;

public class CommandFactory {

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
    return new CustomerInformation(request.customerName, request.customerEmail, request.customerAddress);
  }

}
