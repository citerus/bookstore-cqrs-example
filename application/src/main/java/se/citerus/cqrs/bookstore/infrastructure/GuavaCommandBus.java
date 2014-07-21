package se.citerus.cqrs.bookstore.infrastructure;

import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;
import se.citerus.cqrs.bookstore.command.Command;
import se.citerus.cqrs.bookstore.command.CommandBus;
import se.citerus.cqrs.bookstore.command.CommandHandler;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GuavaCommandBus implements CommandBus {

  private final EventBus commandBus;

  private GuavaCommandBus(EventBus commandBus) {
    this.commandBus = commandBus;
  }

  public static CommandBus asyncGuavaCommandBus() {
    ExecutorService executorService = Executors.newFixedThreadPool(1);
    return new GuavaCommandBus(new AsyncEventBus("commandbus", executorService));
  }

  public static CommandBus syncGuavaCommandBus() {
    return new GuavaCommandBus(new EventBus("commandbus"));
  }

  @Override
  public <T extends CommandHandler> T register(T handler) {
    commandBus.register(handler);
    return handler;
  }

  @Override
  public void dispatch(Command command) {
    commandBus.post(command);
  }

}
