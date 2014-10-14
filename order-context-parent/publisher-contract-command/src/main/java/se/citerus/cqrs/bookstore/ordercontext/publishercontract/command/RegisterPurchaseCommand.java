package se.citerus.cqrs.bookstore.ordercontext.publishercontract.command;

import se.citerus.cqrs.bookstore.command.Command;
import se.citerus.cqrs.bookstore.ordercontext.order.ProductId;
import se.citerus.cqrs.bookstore.ordercontext.publishercontract.PublisherContractId;

import static com.google.common.base.Preconditions.checkArgument;

public class RegisterPurchaseCommand extends Command {

  public final PublisherContractId publisherContractId;
  public final ProductId productId;
  public final long amount;

  public RegisterPurchaseCommand(PublisherContractId publisherContractId, ProductId productId, long amount) {
    checkArgument(publisherContractId != null, "PublisherContractId cannot be null");
    checkArgument(productId != null, "ProductId cannot be null");
    checkArgument(amount > 0, "Amount must be a positive number");

    this.publisherContractId = publisherContractId;
    this.productId = productId;
    this.amount = amount;
  }

}
