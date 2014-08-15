package se.citerus.cqrs.bookstore.command.publisher;

import se.citerus.cqrs.bookstore.book.BookId;
import se.citerus.cqrs.bookstore.command.Command;
import se.citerus.cqrs.bookstore.publisher.PublisherContractId;

import static com.google.common.base.Preconditions.checkArgument;

public class RegisterPurchaseCommand extends Command {

  public final PublisherContractId contractId;
  public final BookId bookId;
  public final long amount;

  public RegisterPurchaseCommand(PublisherContractId contractId, BookId bookId, long amount) {
    checkArgument(contractId != null, "PublisherId cannot be null");
    checkArgument(bookId != null, "BookId cannot be null");
    checkArgument(amount > 0, "Amount must be a positive number");

    this.contractId = contractId;
    this.bookId = bookId;
    this.amount = amount;
  }

}
