package se.citerus.cqrs.bookstore.command;

public interface CommandBus {

  void dispatch(Command command);

  <T extends CommandHandler> T register(T handler);

}
