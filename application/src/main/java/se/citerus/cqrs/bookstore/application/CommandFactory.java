package se.citerus.cqrs.bookstore.application;

import se.citerus.cqrs.bookstore.application.web.model.Cart;
import se.citerus.cqrs.bookstore.application.web.model.LineItem;
import se.citerus.cqrs.bookstore.application.web.transport.*;
import se.citerus.cqrs.bookstore.book.BookId;
import se.citerus.cqrs.bookstore.command.book.CreateBookCommand;
import se.citerus.cqrs.bookstore.command.book.UpdateBookPriceCommand;
import se.citerus.cqrs.bookstore.command.order.ActivateOrderCommand;
import se.citerus.cqrs.bookstore.command.order.PlaceOrderCommand;
import se.citerus.cqrs.bookstore.command.publisher.RegisterPublisherContractCommand;
import se.citerus.cqrs.bookstore.order.CustomerInformation;
import se.citerus.cqrs.bookstore.order.OrderId;
import se.citerus.cqrs.bookstore.order.OrderLine;
import se.citerus.cqrs.bookstore.publisher.PublisherContractId;

import java.util.ArrayList;
import java.util.List;

public class CommandFactory {

  public PlaceOrderCommand toCommand(Cart cart, PlaceOrderRequest request) {
    List<OrderLine> itemsToOrder = getOrderLines(cart);
    CustomerInformation customerInformation = getCustomerInformation(request);
    return new PlaceOrderCommand(new OrderId(request.orderId), customerInformation, itemsToOrder);
  }

  public ActivateOrderCommand toCommand(OrderActivationRequest request) {
    return new ActivateOrderCommand(new OrderId(request.orderId));
  }

  public CreateBookCommand toCommand(BookId bookId, CreateBookRequest request) {
    String idString = request.publisherContractId;
    PublisherContractId publisherContractId = idString == null ? null : new PublisherContractId(idString);
    return new CreateBookCommand(bookId, request.isbn, request.title, request.description, request.price, publisherContractId);
  }

  public RegisterPublisherContractCommand toCommand(PublisherContractId publisherContractId, RegisterPublisherRequest request) {
    return new RegisterPublisherContractCommand(publisherContractId, request.publisherName, request.fee, request.limit);
  }

  public UpdateBookPriceCommand toCommand(UpdateBookPriceRequest updateBookPriceRequest) {
    return new UpdateBookPriceCommand(new BookId(updateBookPriceRequest.bookId), updateBookPriceRequest.price);
  }

  private List<OrderLine> getOrderLines(Cart cart) {
    List<OrderLine> itemsToOrder = new ArrayList<>();
    for (LineItem lineItem : cart.getItems()) {
      BookId bookId = lineItem.getItem().bookId;
      String title = lineItem.getItem().title;
      int quantity = lineItem.getQuantity();
      long price = lineItem.getPrice();
      itemsToOrder.add(new OrderLine(bookId, title, quantity, price, null));
    }
    return itemsToOrder;
  }

  private CustomerInformation getCustomerInformation(PlaceOrderRequest request) {
    return new CustomerInformation(request.customerName,
        request.customerEmail, request.customerAddress);
  }

}
