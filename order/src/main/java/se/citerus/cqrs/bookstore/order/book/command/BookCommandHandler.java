package se.citerus.cqrs.bookstore.order.book.command;

import com.google.common.eventbus.Subscribe;
import se.citerus.cqrs.bookstore.command.CommandHandler;
import se.citerus.cqrs.bookstore.domain.Repository;
import se.citerus.cqrs.bookstore.order.book.domain.Book;

public class BookCommandHandler implements CommandHandler {

  private final Repository repository;

  public BookCommandHandler(Repository repository) {
    this.repository = repository;
  }

  @Subscribe
  public void handle(CreateBookCommand command) {
    Book book = new Book();
    book.create(command.bookId, command.isbn, command.title, command.description, command.price, command.publisherContractId);
    repository.save(book);
  }

  @Subscribe
  public void handle(UpdateBookPriceCommand command) {
    Book book = repository.load(command.bookId, Book.class);
    book.updatePrice(command.price);
    repository.save(book);
  }

}
