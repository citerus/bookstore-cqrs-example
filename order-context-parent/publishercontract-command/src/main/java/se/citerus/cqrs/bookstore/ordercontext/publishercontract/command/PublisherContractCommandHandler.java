package se.citerus.cqrs.bookstore.ordercontext.publishercontract.command;

import com.google.common.eventbus.Subscribe;
import se.citerus.cqrs.bookstore.command.CommandHandler;
import se.citerus.cqrs.bookstore.domain.Repository;
import se.citerus.cqrs.bookstore.ordercontext.publishercontract.domain.PublisherContract;

public class PublisherContractCommandHandler implements CommandHandler {

  private final Repository repository;

  public PublisherContractCommandHandler(Repository repository) {
    this.repository = repository;
  }

  @Subscribe
  public void handle(RegisterPublisherContractCommand command) {
    PublisherContract contract = new PublisherContract();
    contract.register(command.publisherContractId, command.publisherName, command.feePercentage, command.limit);
    repository.save(contract);
  }

  @Subscribe
  public void handle(RegisterPurchaseCommand command) {
    PublisherContract contract = repository.load(command.publisherContractId, PublisherContract.class);
    contract.registerPurchase(command.bookId, command.amount);
    repository.save(contract);
  }

}
