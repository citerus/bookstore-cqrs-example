package se.citerus.cqrs.bookstore.command.publisher;

import com.google.common.eventbus.Subscribe;
import se.citerus.cqrs.bookstore.command.CommandHandler;
import se.citerus.cqrs.bookstore.domain.Repository;
import se.citerus.cqrs.bookstore.domain.publisher.PublisherContract;

public class PublisherCommandHandler implements CommandHandler {

  private final Repository repository;

  public PublisherCommandHandler(Repository repository) {
    this.repository = repository;
  }

  @Subscribe
  public void handle(RegisterPublisherCommand command) {
    PublisherContract contract = new PublisherContract();
    contract.register(command.publisherId, command.publisherName, command.fee);
    repository.save(contract);
  }

  @Subscribe
  public void handle(UpdatePublisherFeeCommand command) {
    PublisherContract contract = repository.load(command.publisherId, PublisherContract.class);
    contract.updateFee(command.fee);
    repository.save(contract);
  }

  @Subscribe
  public void handle(RegisterPurchaseCommand command) {
    PublisherContract contract = repository.load(command.publisherId, PublisherContract.class);
    contract.registerPurchase(command.bookId, command.amount);
    repository.save(contract);
  }

}
