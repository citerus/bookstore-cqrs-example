package se.citerus.cqrs.bookstore.command.publisher;

import se.citerus.cqrs.bookstore.command.Command;
import se.citerus.cqrs.bookstore.publisher.PublisherId;

import static com.google.common.base.Preconditions.checkArgument;

public class UpdatePublisherFeeCommand extends Command {

  public final PublisherId publisherId;
  public final long fee;

  public UpdatePublisherFeeCommand(PublisherId publisherId, long fee) {
    checkArgument(publisherId != null, "PublisherId cannot be null");
    checkArgument(fee > 0, "Fee must be a positive number");

    this.publisherId = publisherId;
    this.fee = fee;
  }

}
