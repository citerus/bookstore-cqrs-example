package se.citerus.cqrs.bookstore.ordercontext.publishercontract.command;

import se.citerus.cqrs.bookstore.command.Command;
import se.citerus.cqrs.bookstore.ordercontext.order.ProductId;
import se.citerus.cqrs.bookstore.ordercontext.publishercontract.PublisherContractId;

import static com.google.common.base.Preconditions.checkArgument;

public class RegisterPurchaseCommand extends Command {

  public final PublisherContractId publisherContractId;
  public final ProductId productId;
  public final long unitPrice;
  public final int quantity;

  public RegisterPurchaseCommand(PublisherContractId publisherContractId, ProductId productId, long unitPrice, int quantity) {
    checkArgument(publisherContractId != null, "PublisherContractId cannot be null");
    checkArgument(productId != null, "ProductId cannot be null");
    checkArgument(unitPrice > 0, "UnitPrice must be a positive number");
    checkArgument(quantity > 0, "Quantity must be a positive number");

    this.publisherContractId = publisherContractId;
    this.productId = productId;
    this.unitPrice = unitPrice;
    this.quantity = quantity;
  }

}
