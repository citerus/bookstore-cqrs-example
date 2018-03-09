package se.citerus.cqrs.bookstore.shopping.application;

import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.client.JerseyClientBuilder;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.citerus.cqrs.bookstore.shopping.client.productcatalog.ProductCatalogClient;
import se.citerus.cqrs.bookstore.shopping.domain.CartRepository;
import se.citerus.cqrs.bookstore.shopping.infrastructure.InMemoryCartRepository;
import se.citerus.cqrs.bookstore.shopping.resource.CartResource;

import javax.ws.rs.client.Client;

public class ShoppingApplication extends Application<ShoppingConfiguration> {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  @Override
  public void initialize(Bootstrap<ShoppingConfiguration> bootstrap) {
    bootstrap.addBundle(new AssetsBundle("/assets/", "/", "index.html"));
  }

  @Override
  public void run(ShoppingConfiguration configuration, Environment environment) throws Exception {
    CartRepository cartRepository = new InMemoryCartRepository();
    final Client client = new JerseyClientBuilder(environment).using(configuration.httpClient).build(getName());
    ProductCatalogClient productCatalogClient = ProductCatalogClient.create(client,
        configuration.productCatalogServiceUrl);
    environment.jersey().register(new CartResource(productCatalogClient, cartRepository));
    environment.jersey().setUrlPattern("/service/*");
    logger.info("ShoppingApplication started!");
  }

  public static void main(String[] args) throws Exception {
    new ShoppingApplication().run(args);
  }

}
