package se.citerus.cqrs.bookstore.command.publisher;

import se.citerus.cqrs.bookstore.command.Command;
import se.citerus.cqrs.bookstore.publisher.PublisherId;

import static com.google.common.base.Preconditions.checkArgument;

public class RegisterPublisherCommand extends Command {

  public final PublisherId publisherId;
  public final String publisherName;
  public final double fee;

  public RegisterPublisherCommand(PublisherId publisherId, String publisherName, double fee) {
    checkArgument(publisherId != null, "PublisherId cannot be null");
    checkArgument(publisherName != null, "PublisherName cannot be null");
    checkArgument(fee > 0, "Fee must be a positive number");

    this.publisherId = publisherId;
    this.publisherName = publisherName;
    this.fee = fee;
  }

}
