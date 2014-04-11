package se.citerus.cqrs.bookstore.command.publisher;

import se.citerus.cqrs.bookstore.book.BookId;
import se.citerus.cqrs.bookstore.command.Command;
import se.citerus.cqrs.bookstore.publisher.PublisherId;

import static com.google.common.base.Preconditions.checkArgument;

public class RegisterPurchaseCommand extends Command {

  public final PublisherId publisherId;
  public final BookId bookId;
  public final long totalPrice;

  public RegisterPurchaseCommand(PublisherId publisherId, BookId bookId, long totalPrice) {
    checkArgument(publisherId != null, "PublisherId cannot be null");
    checkArgument(bookId != null, "BookId cannot be null");
    checkArgument(totalPrice > 0, "TotalPrice must be a positive number");

    this.publisherId = publisherId;
    this.bookId = bookId;
    this.totalPrice = totalPrice;
  }

}
