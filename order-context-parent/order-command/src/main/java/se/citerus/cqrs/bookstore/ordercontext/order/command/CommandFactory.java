package se.citerus.cqrs.bookstore.ordercontext.order.command;

import se.citerus.cqrs.bookstore.ordercontext.api.CartDto;
import se.citerus.cqrs.bookstore.ordercontext.api.LineItemDto;
import se.citerus.cqrs.bookstore.ordercontext.api.OrderActivationRequest;
import se.citerus.cqrs.bookstore.ordercontext.api.PlaceOrderRequest;
import se.citerus.cqrs.bookstore.ordercontext.order.CustomerInformation;
import se.citerus.cqrs.bookstore.ordercontext.order.OrderId;
import se.citerus.cqrs.bookstore.ordercontext.order.OrderLine;
import se.citerus.cqrs.bookstore.ordercontext.order.ProductId;

import java.util.ArrayList;
import java.util.List;

public class CommandFactory {

  public PlaceOrderCommand toCommand(CartDto cart, PlaceOrderRequest request) {
    List<OrderLine> itemsToOrder = getOrderLines(cart);
    CustomerInformation customerInformation = getCustomerInformation(request);
    long totalPrice = request.cart.totalPrice;
    return new PlaceOrderCommand(new OrderId(request.orderId), customerInformation, itemsToOrder, totalPrice);
  }

  public ActivateOrderCommand toCommand(OrderActivationRequest request) {
    return new ActivateOrderCommand(new OrderId(request.orderId));
  }

  private List<OrderLine> getOrderLines(CartDto cart) {
    List<OrderLine> itemsToOrder = new ArrayList<>();
    for (LineItemDto lineItem : cart.lineItems) {
      ProductId productId = new ProductId(lineItem.productId);
      String title = lineItem.title;
      int quantity = lineItem.quantity;
      long price = lineItem.price;
      itemsToOrder.add(new OrderLine(productId, title, quantity, price));
    }
    return itemsToOrder;
  }

  private CustomerInformation getCustomerInformation(PlaceOrderRequest request) {
    return new CustomerInformation(request.customerName, request.customerEmail, request.customerAddress);
  }

}
