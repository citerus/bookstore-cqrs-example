package se.citerus.cqrs.bookstore.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.jersey.api.client.Client;
import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.citerus.cqrs.bookstore.admin.client.order.OrderClient;
import se.citerus.cqrs.bookstore.admin.resource.AdminResource;
import se.citerus.cqrs.bookstore.bookcatalog.infrastructure.InMemoryBookRepository;
import se.citerus.cqrs.bookstore.bookcatalog.resource.BookResource;
import se.citerus.cqrs.bookstore.command.CommandBus;
import se.citerus.cqrs.bookstore.domain.Repository;
import se.citerus.cqrs.bookstore.event.DomainEventBus;
import se.citerus.cqrs.bookstore.event.DomainEventStore;
import se.citerus.cqrs.bookstore.infrastructure.DefaultRepository;
import se.citerus.cqrs.bookstore.infrastructure.GuavaCommandBus;
import se.citerus.cqrs.bookstore.infrastructure.GuavaDomainEventBus;
import se.citerus.cqrs.bookstore.ordercontext.infrastructure.InMemOrderProjectionRepository;
import se.citerus.cqrs.bookstore.ordercontext.order.command.OrderCommandHandler;
import se.citerus.cqrs.bookstore.ordercontext.publishercontract.command.PublisherContractCommandHandler;
import se.citerus.cqrs.bookstore.ordercontext.query.QueryService;
import se.citerus.cqrs.bookstore.ordercontext.query.orderlist.OrderListDenormalizer;
import se.citerus.cqrs.bookstore.ordercontext.query.orderlist.OrderProjectionRepository;
import se.citerus.cqrs.bookstore.ordercontext.query.sales.OrdersPerDayAggregator;
import se.citerus.cqrs.bookstore.ordercontext.resource.OrderResource;
import se.citerus.cqrs.bookstore.ordercontext.resource.PublisherContractResource;
import se.citerus.cqrs.bookstore.ordercontext.resource.QueryResource;
import se.citerus.cqrs.bookstore.ordercontext.saga.PurchaseRegistrationSaga;
import se.citerus.cqrs.bookstore.shopping.client.bookcatalog.BookClient;
import se.citerus.cqrs.bookstore.shopping.domain.CartRepository;
import se.citerus.cqrs.bookstore.shopping.infrastructure.InMemoryCartRepository;
import se.citerus.cqrs.bookstore.shopping.resource.CartResource;

import static com.fasterxml.jackson.databind.ObjectMapper.DefaultTyping.NON_FINAL;
import static com.fasterxml.jackson.databind.ObjectMapper.DefaultTyping.OBJECT_AND_NON_CONCRETE;
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
    DomainEventBus domainEventBus = new GuavaDomainEventBus();
    OrderProjectionRepository orderRepository = new InMemOrderProjectionRepository();
    OrderListDenormalizer orderListDenormalizer = domainEventBus.register(new OrderListDenormalizer(orderRepository));
    OrdersPerDayAggregator ordersPerDayAggregator = domainEventBus.register(new OrdersPerDayAggregator());

    se.citerus.cqrs.bookstore.ordercontext.client.bookcatalog.BookCatalogClient bookCatalogClient = se.citerus.cqrs.bookstore.ordercontext.client.bookcatalog.BookCatalogClient.create(Client.create());
    QueryService queryService = new QueryService(orderListDenormalizer, ordersPerDayAggregator, bookCatalogClient);

    DomainEventStore domainEventStore = (DomainEventStore) bookstoreConfiguration.eventStore.newInstance();
    logger.info("Using eventStore: " + domainEventStore.getClass().getName());
    Repository aggregateRepository = new DefaultRepository(domainEventBus, domainEventStore);

    CommandBus commandBus = GuavaCommandBus.asyncGuavaCommandBus();
    commandBus.register(new OrderCommandHandler(aggregateRepository, queryService));
    commandBus.register(new PublisherContractCommandHandler(aggregateRepository));

    // Create and register Sagas
    PurchaseRegistrationSaga purchaseRegistrationSaga = new PurchaseRegistrationSaga(queryService, commandBus);
    domainEventBus.register(purchaseRegistrationSaga);

    BookClient bookClient = BookClient.create(Client.create());
    OrderClient orderClient = OrderClient.create(Client.create());

    environment.jersey().register(new OrderResource(commandBus));
    environment.jersey().register(new BookResource(new InMemoryBookRepository()));
    environment.jersey().register(new CartResource(bookClient, cartRepository));
    environment.jersey().register(new AdminResource(orderClient));
    environment.jersey().register(new PublisherContractResource(commandBus));
    environment.jersey().register(new QueryResource(queryService, domainEventStore));
    logger.info("Server started!");
  }

  public static void main(String[] args) throws Exception {
    new BookstoreApplication().run(args);
  }
}
