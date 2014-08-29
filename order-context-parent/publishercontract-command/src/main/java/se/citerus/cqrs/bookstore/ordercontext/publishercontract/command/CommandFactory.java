package se.citerus.cqrs.bookstore.ordercontext.publishercontract.command;

import se.citerus.cqrs.bookstore.ordercontext.api.RegisterPublisherContractRequest;
import se.citerus.cqrs.bookstore.ordercontext.publishercontract.PublisherContractId;

public class CommandFactory {

  public RegisterPublisherContractCommand toCommand(PublisherContractId contractId, RegisterPublisherContractRequest request) {
    return new RegisterPublisherContractCommand(contractId, request.publisherName, request.feePercentage,
        request.limit);
  }

}
