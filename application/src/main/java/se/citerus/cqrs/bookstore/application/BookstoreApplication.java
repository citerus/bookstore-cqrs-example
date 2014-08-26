package se.citerus.cqrs.bookstore.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.jersey.api.client.Client;
import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.citerus.cqrs.bookstore.admin.client.OrderClient;
import se.citerus.cqrs.bookstore.admin.client.PublisherClient;
import se.citerus.cqrs.bookstore.admin.web.AdminResource;
import se.citerus.cqrs.bookstore.application.web.BookResource;
import se.citerus.cqrs.bookstore.application.web.CartClient;
import se.citerus.cqrs.bookstore.application.web.OrderResource;
import se.citerus.cqrs.bookstore.application.web.PublisherResource;
import se.citerus.cqrs.bookstore.command.CommandBus;
import se.citerus.cqrs.bookstore.domain.Repository;
import se.citerus.cqrs.bookstore.event.DomainEventBus;
import se.citerus.cqrs.bookstore.event.DomainEventStore;
import se.citerus.cqrs.bookstore.infrastructure.*;
import se.citerus.cqrs.bookstore.order.book.command.BookCommandHandler;
import se.citerus.cqrs.bookstore.order.command.OrderCommandHandler;
import se.citerus.cqrs.bookstore.order.publisher.command.PublisherContractCommandHandler;
import se.citerus.cqrs.bookstore.order.saga.PurchaseRegistrationSaga;
import se.citerus.cqrs.bookstore.query.BookCatalogDenormalizer;
import se.citerus.cqrs.bookstore.query.OrderListDenormalizer;
import se.citerus.cqrs.bookstore.query.OrdersPerDayAggregator;
import se.citerus.cqrs.bookstore.query.QueryService;
import se.citerus.cqrs.bookstore.query.repository.InMemOrderProjectionRepository;
import se.citerus.cqrs.bookstore.shopping.web.CartResource;
import se.citerus.cqrs.bookstore.shopping.web.model.CartRepository;

import java.net.URISyntaxException;

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
  public void run(BookstoreConfiguration bookstoreConfiguration, Environment environment) throws URISyntaxException {

    logger.info("Starting cqrs-bookstore server...");
    logger.info("Creating/registering denormalizers");

    ObjectMapper objectMapper = environment.getObjectMapper();
    objectMapper.enable(INDENT_OUTPUT);
    objectMapper.enable(WRITE_DATES_AS_TIMESTAMPS);

    CartRepository cartRepository = new InMemoryCartRepository();
    DomainEventBus domainEventBus = new GuavaDomainEventBus();
    InMemOrderProjectionRepository orderRepository = new InMemOrderProjectionRepository();
    OrderListDenormalizer orderListDenormalizer = domainEventBus.register(new OrderListDenormalizer(orderRepository));
    BookCatalogDenormalizer bookCatalogDenormalizer = domainEventBus.register(new BookCatalogDenormalizer());
    OrdersPerDayAggregator ordersPerDayAggregator = domainEventBus.register(new OrdersPerDayAggregator());

    QueryService queryService = new QueryService(orderListDenormalizer, bookCatalogDenormalizer, ordersPerDayAggregator);

    DomainEventStore domainEventStore = new InMemoryDomainEventStore();
    Repository aggregateRepository = new DefaultRepository(domainEventBus, domainEventStore);

    CommandBus commandBus = GuavaCommandBus.asyncGuavaCommandBus();
    commandBus.register(new OrderCommandHandler(aggregateRepository, queryService));
    commandBus.register(new BookCommandHandler(aggregateRepository));
    commandBus.register(new PublisherContractCommandHandler(aggregateRepository));

    // Create and register Sagas
    PurchaseRegistrationSaga purchaseRegistrationSaga = new PurchaseRegistrationSaga(queryService, commandBus);
    domainEventBus.register(purchaseRegistrationSaga);

    CartClient cartClient = CartClient.create(Client.create());
    OrderClient orderClient = OrderClient.create(Client.create());
    PublisherClient publisherClient = PublisherClient.create(Client.create());
    environment.jersey().register(new OrderResource(commandBus, cartClient));
    environment.jersey().register(new BookResource(queryService));
    environment.jersey().register(new CartResource(queryService, cartRepository));
    environment.jersey().register(new AdminResource(queryService, commandBus, domainEventStore, orderClient, publisherClient));
    environment.jersey().register(new PublisherResource(commandBus));
    logger.info("Server started!");
  }

  public static void main(String[] args) throws Exception {
    new BookstoreApplication().run(args);
  }
}
