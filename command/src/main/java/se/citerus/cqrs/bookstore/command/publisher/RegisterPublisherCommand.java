package se.citerus.cqrs.bookstore.command.publisher;

import se.citerus.cqrs.bookstore.command.Command;
import se.citerus.cqrs.bookstore.publisher.PublisherContractId;

import static com.google.common.base.Preconditions.checkArgument;

public class RegisterPublisherCommand extends Command {

  public final PublisherContractId contractId;
  public final String publisherName;
  public final double fee;

  public RegisterPublisherCommand(PublisherContractId contractId, String publisherName, double fee) {
    checkArgument(contractId != null, "PublisherId cannot be null");
    checkArgument(publisherName != null, "PublisherName cannot be null");
    checkArgument(fee > 0, "Fee must be a positive number");

    this.contractId = contractId;
    this.publisherName = publisherName;
    this.fee = fee;
  }

}
