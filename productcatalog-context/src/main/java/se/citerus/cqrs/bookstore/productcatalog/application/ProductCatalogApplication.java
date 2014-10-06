package se.citerus.cqrs.bookstore.productcatalog.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.citerus.cqrs.bookstore.productcatalog.infrastructure.InMemoryProductRepository;
import se.citerus.cqrs.bookstore.productcatalog.resource.ProductResource;

import static com.fasterxml.jackson.databind.SerializationFeature.INDENT_OUTPUT;
import static com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS;

public class ProductCatalogApplication extends Application<ProductCatalogConfig> {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  @Override
  public void initialize(Bootstrap<ProductCatalogConfig> bootstrap) {
  }

  @Override
  public void run(ProductCatalogConfig configuration, Environment environment) {
    ObjectMapper objectMapper = environment.getObjectMapper();
    objectMapper.enable(INDENT_OUTPUT);
    objectMapper.enable(WRITE_DATES_AS_TIMESTAMPS);

    environment.jersey().register(new ProductResource(new InMemoryProductRepository()));
    logger.info("ProductCatalogApplication started!");
  }

  public static void main(String[] args) throws Exception {
    new ProductCatalogApplication().run(args);
  }

}
