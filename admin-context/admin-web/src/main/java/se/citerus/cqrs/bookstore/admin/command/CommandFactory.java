package se.citerus.cqrs.bookstore.admin.command;

import se.citerus.cqrs.bookstore.admin.web.transport.CreateBookRequest;
import se.citerus.cqrs.bookstore.admin.web.transport.UpdateBookPriceRequest;
import se.citerus.cqrs.bookstore.book.BookId;
import se.citerus.cqrs.bookstore.order.book.command.CreateBookCommand;
import se.citerus.cqrs.bookstore.order.book.command.UpdateBookPriceCommand;
import se.citerus.cqrs.bookstore.publisher.PublisherContractId;

public class CommandFactory {

  public CreateBookCommand toCommand(BookId bookId, CreateBookRequest request) {
    String idString = request.publisherContractId;
    PublisherContractId publisherContractId = idString == null ? null : new PublisherContractId(idString);
    return new CreateBookCommand(bookId, request.isbn, request.title, request.description, request.price, publisherContractId);
  }

  public UpdateBookPriceCommand toCommand(UpdateBookPriceRequest updateBookPriceRequest) {
    return new UpdateBookPriceCommand(new BookId(updateBookPriceRequest.bookId), updateBookPriceRequest.price);
  }

}
