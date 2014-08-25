package se.citerus.cqrs.bookstore.order.book.command;

import se.citerus.cqrs.bookstore.book.BookId;
import se.citerus.cqrs.bookstore.command.Command;

import static com.google.common.base.Preconditions.checkArgument;

public class UpdateBookPriceCommand extends Command {

  public final BookId bookId;
  public final long price;

  public UpdateBookPriceCommand(BookId bookId, long price) {
    checkArgument(bookId != null, "BookId cannot be null");
    checkArgument(price > 0, "Price must be a positive number");

    this.bookId = bookId;
    this.price = price;
  }

}
