package se.citerus.cqrs.bookstore.shopping.application;

import io.dropwizard.Configuration;
import io.dropwizard.client.JerseyClientConfiguration;

public class ShoppingConfiguration extends Configuration {

  public String productCatalogServiceUrl;

  public JerseyClientConfiguration httpClient = new JerseyClientConfiguration();
}
