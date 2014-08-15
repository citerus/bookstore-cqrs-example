package se.citerus.cqrs.bookstore.command.publisher;

import se.citerus.cqrs.bookstore.command.Command;
import se.citerus.cqrs.bookstore.publisher.PublisherContractId;

import static com.google.common.base.Preconditions.checkArgument;

public class UpdatePublisherFeeCommand extends Command {

  public final PublisherContractId contractId;
  public final long fee;

  public UpdatePublisherFeeCommand(PublisherContractId contractId, long fee) {
    checkArgument(contractId != null, "PublisherId cannot be null");
    checkArgument(fee > 0, "Fee must be a positive number");

    this.contractId = contractId;
    this.fee = fee;
  }

}
