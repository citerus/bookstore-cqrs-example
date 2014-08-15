package se.citerus.cqrs.bookstore.command.book;

import se.citerus.cqrs.bookstore.book.BookId;
import se.citerus.cqrs.bookstore.command.Command;
import se.citerus.cqrs.bookstore.publisher.PublisherContractId;

import static com.google.common.base.Preconditions.checkArgument;

public class CreateBookCommand extends Command {

  public final BookId bookId;
  public final String isbn;
  public final String title;
  public final String description;
  public final long price;
  public final PublisherContractId publisherContractId;

  public CreateBookCommand(BookId bookId, String isbn, String title, String description, long price,
                           PublisherContractId publisherContractId) {
    checkArgument(bookId != null, "BookId cannot be null");
    checkArgument(isbn != null, "ISBN cannot be null");
    checkArgument(title != null, "Title cannot be null");
    checkArgument(description != null, "Description cannot be null");
    checkArgument(price > 0, "Price must be a positive number");

    this.bookId = bookId;
    this.isbn = isbn;
    this.title = title;
    this.description = description;
    this.price = price;
    this.publisherContractId = publisherContractId; // May be null!
  }

}
