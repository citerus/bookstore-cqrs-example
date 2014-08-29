package se.citerus.cqrs.bookstore.order.publisher.command;

import se.citerus.cqrs.bookstore.order.publisher.api.RegisterPublisherContractRequest;
import se.citerus.cqrs.bookstore.publisher.PublisherContractId;

public class CommandFactory {

  public RegisterPublisherContractCommand toCommand(PublisherContractId contractId, RegisterPublisherContractRequest request) {
    return new RegisterPublisherContractCommand(contractId, request.publisherName, request.feePercentage,
        request.limit);
  }

}
