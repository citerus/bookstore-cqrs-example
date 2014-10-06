package se.citerus.cqrs.bookstore.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.jersey.api.client.Client;
import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.citerus.cqrs.bookstore.shopping.client.productcatalog.ProductCatalogClient;
import se.citerus.cqrs.bookstore.shopping.domain.CartRepository;
import se.citerus.cqrs.bookstore.shopping.infrastructure.InMemoryCartRepository;
import se.citerus.cqrs.bookstore.shopping.resource.CartResource;

import static com.fasterxml.jackson.databind.SerializationFeature.INDENT_OUTPUT;
import static com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS;

public class BookstoreApplication extends Application<BookstoreConfiguration> {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  @Override
  public void initialize(Bootstrap<BookstoreConfiguration> bootstrap) {
    bootstrap.addBundle(new AssetsBundle("/assets/", "/", "index.html"));
  }

  @Override
  public String getName() {
    return "cqrs-bookstore";
  }

  @Override
  public void run(BookstoreConfiguration bookstoreConfiguration, Environment environment) throws Exception {

    logger.info("Starting cqrs-bookstore server...");
    logger.info("Creating/registering denormalizers");

    environment.jersey().setUrlPattern("/service/*");

    ObjectMapper objectMapper = environment.getObjectMapper();
    objectMapper.enable(INDENT_OUTPUT);
    objectMapper.enable(WRITE_DATES_AS_TIMESTAMPS);

    CartRepository cartRepository = new InMemoryCartRepository();

    ProductCatalogClient productCatalogClient = ProductCatalogClient.create(Client.create());

    environment.jersey().register(new CartResource(productCatalogClient, cartRepository));

    logger.info("Server started!");
  }

  public static void main(String[] args) throws Exception {
    new BookstoreApplication().run(args);
  }
}
