package se.citerus.cqrs.bookstore.ordercontext.publishercontract.command;

import se.citerus.cqrs.bookstore.ordercontext.publishercontract.PublisherContractId;
import se.citerus.cqrs.bookstore.ordercontext.publishercontract.api.RegisterPublisherContractRequest;

public class CommandFactory {

  public RegisterPublisherContractCommand toCommand(PublisherContractId contractId, RegisterPublisherContractRequest request) {
    return new RegisterPublisherContractCommand(contractId, request.publisherName, request.feePercentage,
        request.limit);
  }

}
