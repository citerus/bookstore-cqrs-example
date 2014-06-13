package se.citerus.cqrs.bookstore.command.publisher;

import se.citerus.cqrs.bookstore.book.BookId;
import se.citerus.cqrs.bookstore.command.Command;
import se.citerus.cqrs.bookstore.publisher.PublisherId;

import static com.google.common.base.Preconditions.checkArgument;

public class RegisterPurchaseCommand extends Command {

  public final PublisherId publisherId;
  public final BookId bookId;
  public final long amount;

  public RegisterPurchaseCommand(PublisherId publisherId, BookId bookId, long amount) {
    checkArgument(publisherId != null, "PublisherId cannot be null");
    checkArgument(bookId != null, "BookId cannot be null");
    checkArgument(amount > 0, "Amount must be a positive number");

    this.publisherId = publisherId;
    this.bookId = bookId;
    this.amount = amount;
  }

}
