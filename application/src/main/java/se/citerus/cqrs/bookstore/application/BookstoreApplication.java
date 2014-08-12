package se.citerus.cqrs.bookstore.application;

import com.yammer.dropwizard.Service;
import com.yammer.dropwizard.assets.AssetsBundle;
import com.yammer.dropwizard.config.Bootstrap;
import com.yammer.dropwizard.config.Environment;
import com.yammer.dropwizard.json.ObjectMapperFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.citerus.cqrs.bookstore.application.web.AdminResource;
import se.citerus.cqrs.bookstore.application.web.BookResource;
import se.citerus.cqrs.bookstore.application.web.CartResource;
import se.citerus.cqrs.bookstore.application.web.OrderResource;
import se.citerus.cqrs.bookstore.application.web.model.CartRepository;
import se.citerus.cqrs.bookstore.command.CommandBus;
import se.citerus.cqrs.bookstore.command.book.BookCommandHandler;
import se.citerus.cqrs.bookstore.command.order.OrderCommandHandler;
import se.citerus.cqrs.bookstore.command.publisher.PublisherCommandHandler;
import se.citerus.cqrs.bookstore.domain.Repository;
import se.citerus.cqrs.bookstore.event.DomainEventBus;
import se.citerus.cqrs.bookstore.event.DomainEventStore;
import se.citerus.cqrs.bookstore.infrastructure.DefaultRepository;
import se.citerus.cqrs.bookstore.infrastructure.GuavaCommandBus;
import se.citerus.cqrs.bookstore.infrastructure.GuavaDomainEventBus;
import se.citerus.cqrs.bookstore.infrastructure.InMemoryCartRepository;
import se.citerus.cqrs.bookstore.infrastructure.InMemoryDomainEventStore;
import se.citerus.cqrs.bookstore.query.BookCatalogDenormalizer;
import se.citerus.cqrs.bookstore.query.OrderListDenormalizer;
import se.citerus.cqrs.bookstore.query.OrdersPerDayAggregator;
import se.citerus.cqrs.bookstore.query.PublisherDenormalizer;
import se.citerus.cqrs.bookstore.query.QueryService;
import se.citerus.cqrs.bookstore.query.repository.InMemOrderProjectionRepository;
import se.citerus.cqrs.bookstore.saga.PurchaseRegistrationSaga;

import static com.fasterxml.jackson.databind.SerializationFeature.INDENT_OUTPUT;
import static com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS;

public class BookstoreApplication extends Service<BookstoreConfiguration> {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  @Override
  public void initialize(Bootstrap<BookstoreConfiguration> bootstrap) {
    bootstrap.setName("cqrs-bookstore");
    bootstrap.addBundle(new AssetsBundle("/assets/", "/", "index.html"));
  }

  @Override
  public void run(BookstoreConfiguration bookstoreConfiguration, Environment environment) {

    logger.info("Starting cqrs-bookstore server...");
    logger.info("Creating/registering denormalizers");

    ObjectMapperFactory objectMapper = environment.getObjectMapperFactory();
    objectMapper.enable(INDENT_OUTPUT);
    objectMapper.enable(WRITE_DATES_AS_TIMESTAMPS);

    CartRepository cartRepository = new InMemoryCartRepository();
    DomainEventBus domainEventBus = new GuavaDomainEventBus();
    InMemOrderProjectionRepository orderRepository = new InMemOrderProjectionRepository();
    OrderListDenormalizer orderListDenormalizer = domainEventBus.register(new OrderListDenormalizer(orderRepository));
    PublisherDenormalizer publisherDenormalizer = domainEventBus.register(new PublisherDenormalizer());
    BookCatalogDenormalizer bookCatalogDenormalizer = domainEventBus.register(new BookCatalogDenormalizer());
    OrdersPerDayAggregator ordersPerDayAggregator = domainEventBus.register(new OrdersPerDayAggregator());

    QueryService queryService = new QueryService(orderListDenormalizer, bookCatalogDenormalizer, publisherDenormalizer, ordersPerDayAggregator);

    DomainEventStore domainEventStore = new InMemoryDomainEventStore();
    Repository aggregateRepository = new DefaultRepository(domainEventBus, domainEventStore);

    CommandBus commandBus = GuavaCommandBus.asyncGuavaCommandBus();
    commandBus.register(new OrderCommandHandler(aggregateRepository, queryService));
    commandBus.register(new BookCommandHandler(aggregateRepository));
    commandBus.register(new PublisherCommandHandler(aggregateRepository));

    // Create and register Sagas
    PurchaseRegistrationSaga purchaseRegistrationSaga = new PurchaseRegistrationSaga(queryService, commandBus);
    domainEventBus.register(purchaseRegistrationSaga);

    environment.addResource(new OrderResource(commandBus, cartRepository));
    environment.addResource(new BookResource(queryService));
    environment.addResource(new CartResource(queryService, cartRepository));
    environment.addResource(new AdminResource(queryService, commandBus, domainEventStore));
    logger.info("Server started!");
  }

  public static void main(String[] args) throws Exception {
    new BookstoreApplication().run(args);
  }

}
