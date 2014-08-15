package se.citerus.cqrs.bookstore.command.publisher;

import se.citerus.cqrs.bookstore.command.Command;
import se.citerus.cqrs.bookstore.publisher.PublisherContractId;

import static com.google.common.base.Preconditions.checkArgument;

public class RegisterPublisherContractCommand extends Command {

  public final PublisherContractId publisherContractId;
  public final String publisherName;
  public final double feePercentage;
  public final long limit;

  public RegisterPublisherContractCommand(PublisherContractId publisherContractId, String publisherName, double feePercentage, long limit) {
    checkArgument(publisherContractId != null, "PublisherContractId cannot be null");
    checkArgument(publisherName != null, "PublisherName cannot be null");
    checkArgument(feePercentage > 0, "Fee must be a positive number");
    checkArgument(limit > 0, "Limit must be a positive number");

    this.publisherContractId = publisherContractId;
    this.publisherName = publisherName;
    this.feePercentage = feePercentage;
    this.limit = limit;
  }

}
