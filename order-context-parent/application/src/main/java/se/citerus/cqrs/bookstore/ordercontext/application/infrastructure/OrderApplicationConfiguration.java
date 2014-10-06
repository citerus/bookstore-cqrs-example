package se.citerus.cqrs.bookstore.ordercontext.application.infrastructure;

import io.dropwizard.Configuration;

public class OrderApplicationConfiguration extends Configuration {

  public Class eventStore;

}
