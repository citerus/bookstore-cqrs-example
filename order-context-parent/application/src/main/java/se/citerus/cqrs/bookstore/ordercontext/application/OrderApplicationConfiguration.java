package se.citerus.cqrs.bookstore.ordercontext.application;

import io.dropwizard.Configuration;

public class OrderApplicationConfiguration extends Configuration {

  public Class eventStore;

}
