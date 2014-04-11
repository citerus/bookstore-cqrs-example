package se.citerus.cqrs.bookstore.command.publisher;

import com.google.common.eventbus.Subscribe;
import se.citerus.cqrs.bookstore.command.CommandHandler;
import se.citerus.cqrs.bookstore.domain.Repository;
import se.citerus.cqrs.bookstore.domain.publisher.Publisher;

public class PublisherCommandHandler implements CommandHandler {

  private final Repository repository;

  public PublisherCommandHandler(Repository repository) {
    this.repository = repository;
  }

  @Subscribe
  public void handle(RegisterPublisherCommand command) {
    Publisher publisher = new Publisher();
    publisher.register(command.publisherId, command.publisherName, command.fee);
    repository.save(publisher);
  }

  @Subscribe
  public void handle(UpdatePublisherFeeCommand command) {
    Publisher publisher = repository.load(command.publisherId, Publisher.class);
    publisher.updateFee(command.fee);
    repository.save(publisher);
  }

  @Subscribe
  public void handle(RegisterPurchaseCommand command) {
    Publisher publisher = repository.load(command.publisherId, Publisher.class);
    publisher.registerPurchase(command.bookId, command.totalPrice);
    repository.save(publisher);
  }

}
