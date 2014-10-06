package se.citerus.cqrs.bookstore.ordercontext.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.jersey.api.client.Client;
import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.eclipse.jetty.servlets.CrossOriginFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.citerus.cqrs.bookstore.command.CommandBus;
import se.citerus.cqrs.bookstore.domain.Repository;
import se.citerus.cqrs.bookstore.event.DomainEventBus;
import se.citerus.cqrs.bookstore.event.DomainEventStore;
import se.citerus.cqrs.bookstore.ordercontext.application.infrastructure.DefaultRepository;
import se.citerus.cqrs.bookstore.ordercontext.application.infrastructure.GuavaCommandBus;
import se.citerus.cqrs.bookstore.ordercontext.application.infrastructure.GuavaDomainEventBus;
import se.citerus.cqrs.bookstore.ordercontext.application.task.ReplayEventsTask;
import se.citerus.cqrs.bookstore.ordercontext.client.productcatalog.ProductCatalogClient;
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

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import java.util.EnumSet;

import static com.fasterxml.jackson.databind.SerializationFeature.INDENT_OUTPUT;
import static com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS;

public class OrderApplication extends Application<OrderApplicationConfiguration> {
  private final Logger logger = LoggerFactory.getLogger(getClass());

  @Override
  public void initialize(Bootstrap<OrderApplicationConfiguration> bootstrap) {
    bootstrap.addBundle(new AssetsBundle("/assets/", "/", "admin.html"));
  }

  @Override
  public void run(OrderApplicationConfiguration configuration, Environment environment) throws Exception {
    ObjectMapper objectMapper = environment.getObjectMapper();
    objectMapper.enable(INDENT_OUTPUT);
    objectMapper.enable(WRITE_DATES_AS_TIMESTAMPS);

    OrderProjectionRepository orderRepository = new InMemOrderProjectionRepository();
    DomainEventBus domainEventBus = new GuavaDomainEventBus();
    OrderListDenormalizer orderListDenormalizer = domainEventBus.register(new OrderListDenormalizer(orderRepository));
    OrdersPerDayAggregator ordersPerDayAggregator = domainEventBus.register(new OrdersPerDayAggregator());

    ProductCatalogClient catalogClient = ProductCatalogClient.create(Client.create(), configuration.productCatalogServiceUrl);

    DomainEventStore domainEventStore = (DomainEventStore) configuration.eventStore.newInstance();
    QueryService queryService = new QueryService(orderListDenormalizer, ordersPerDayAggregator, catalogClient);

    logger.info("Using eventStore: " + domainEventStore.getClass().getName());
    Repository aggregateRepository = new DefaultRepository(domainEventBus, domainEventStore);

    CommandBus commandBus = GuavaCommandBus.asyncGuavaCommandBus();
    commandBus.register(new OrderCommandHandler(aggregateRepository, queryService));
    commandBus.register(new PublisherContractCommandHandler(aggregateRepository));

    // Create and register Sagas
    PurchaseRegistrationSaga purchaseRegistrationSaga = new PurchaseRegistrationSaga(queryService, commandBus);
    domainEventBus.register(purchaseRegistrationSaga);

    environment.jersey().register(new QueryResource(queryService, domainEventStore));
    environment.jersey().register(new OrderResource(commandBus));
    environment.jersey().register(new PublisherContractResource(commandBus));
    environment.jersey().setUrlPattern("/service/*");

    environment.admin().addTask(new ReplayEventsTask(domainEventStore, domainEventBus));
    configureCors(environment);
  }

  private void configureCors(Environment environment) {
    FilterRegistration.Dynamic filter = environment.servlets().addFilter("CORS", CrossOriginFilter.class);
    filter.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, "/*");
    filter.setInitParameter(CrossOriginFilter.ALLOWED_METHODS_PARAM, "GET,PUT,POST,DELETE,OPTIONS");
    filter.setInitParameter(CrossOriginFilter.ALLOWED_ORIGINS_PARAM, "*");
    filter.setInitParameter(CrossOriginFilter.ACCESS_CONTROL_ALLOW_ORIGIN_HEADER, "*");
    filter.setInitParameter("allowedHeaders", "Content-Type,Authorization,X-Requested-With,Content-Length,Accept,Origin");
    filter.setInitParameter("allowCredentials", "true");
  }

  public static void main(String[] args) throws Exception {
    new OrderApplication().run(args);
  }

}
