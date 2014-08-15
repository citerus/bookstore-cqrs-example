package se.citerus.cqrs.bookstore.application;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.GenericType;
import com.yammer.dropwizard.testing.ResourceTest;
import org.junit.Test;
import se.citerus.cqrs.bookstore.application.web.AdminResource;
import se.citerus.cqrs.bookstore.application.web.BookResource;
import se.citerus.cqrs.bookstore.application.web.CartResource;
import se.citerus.cqrs.bookstore.application.web.OrderResource;
import se.citerus.cqrs.bookstore.application.web.model.CartRepository;
import se.citerus.cqrs.bookstore.application.web.transport.*;
import se.citerus.cqrs.bookstore.book.BookId;
import se.citerus.cqrs.bookstore.command.CommandBus;
import se.citerus.cqrs.bookstore.command.book.BookCommandHandler;
import se.citerus.cqrs.bookstore.command.order.OrderCommandHandler;
import se.citerus.cqrs.bookstore.command.publisher.PublisherCommandHandler;
import se.citerus.cqrs.bookstore.domain.Repository;
import se.citerus.cqrs.bookstore.event.DomainEventBus;
import se.citerus.cqrs.bookstore.infrastructure.DefaultRepository;
import se.citerus.cqrs.bookstore.infrastructure.GuavaCommandBus;
import se.citerus.cqrs.bookstore.infrastructure.InMemoryCartRepository;
import se.citerus.cqrs.bookstore.infrastructure.InMemoryDomainEventStore;
import se.citerus.cqrs.bookstore.order.CustomerInformation;
import se.citerus.cqrs.bookstore.order.OrderId;
import se.citerus.cqrs.bookstore.order.OrderStatus;
import se.citerus.cqrs.bookstore.publisher.PublisherContractId;
import se.citerus.cqrs.bookstore.query.*;
import se.citerus.cqrs.bookstore.query.repository.InMemOrderProjectionRepository;

import java.util.Collection;
import java.util.Iterator;
import java.util.UUID;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.Status.Family.SUCCESSFUL;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static se.citerus.cqrs.bookstore.order.OrderStatus.ACTIVATED;

public class ScenarioTest extends ResourceTest {

  public static final String SERVER_ADDRESS = "http://localhost:8080";

  @Override
  protected void setUpResources() throws Exception {
    CommandBus commandBus = GuavaCommandBus.syncGuavaCommandBus();

    CartRepository cartRepository = new InMemoryCartRepository();
    OrdersPerDayAggregator ordersPerDayAggregator = new OrdersPerDayAggregator();

    DomainEventBus domainEventBus = new TestEventBus();
    QueryService queryService = createQueryService(ordersPerDayAggregator, domainEventBus);

    InMemoryDomainEventStore domainEventStore = new InMemoryDomainEventStore();
    Repository repository = new DefaultRepository(domainEventBus, domainEventStore);
    BookCommandHandler bookCommandHandler = new BookCommandHandler(repository);
    OrderCommandHandler orderCommandHandler = new OrderCommandHandler(repository, queryService);
    PublisherCommandHandler publisherCommandHandler = new PublisherCommandHandler(repository);

    commandBus.register(bookCommandHandler);
    commandBus.register(orderCommandHandler);
    commandBus.register(publisherCommandHandler);

    addResource(new CartResource(queryService, cartRepository));
    addResource(new OrderResource(commandBus, cartRepository));
    addResource(new BookResource(queryService));
    addResource(new AdminResource(queryService, commandBus, domainEventStore));
  }

  @Test
  public void testCreateBook() {
    CreateBookRequest book = createRandomBook();
    createBook(book);
    BookProjection bookProjection = getBook(book.bookId);
    assertThat(bookProjection.getTitle(), is("DDD"));
    assertThat(bookProjection.getDescription(), is("Domain Driven Design"));
    assertThat(bookProjection.getIsbn(), is(book.isbn));
  }

  @Test
  public void testUpdateBookPrice() throws Exception {
    BookId bookId = BookId.randomId();
    String isbn = "0321144215";
    CreateBookRequest bookRequest = new CreateBookRequest(bookId.id, isbn, "DDD", "Domain Driven Design", 1000, null);
    createBook(bookRequest);

    updateBookPrice(bookId, 500L);

    BookProjection bookProjection = getBook(bookId.id);
    assertThat(bookProjection.getTitle(), is("DDD"));
    assertThat(bookProjection.getDescription(), is("Domain Driven Design"));
    assertThat(bookProjection.getIsbn(), is(isbn));
    assertThat(bookProjection.getPrice(), is(500L));
  }

  @Test
  public void testUpdatePublisherFee() throws Exception {
    BookId bookId = BookId.randomId();
    String isbn = "0321144215";
    PublisherContractId contractId = PublisherContractId.randomId();
    registerPublisher(contractId.id, "Books Inc.", 2.32);
    CreateBookRequest bookRequest = new CreateBookRequest(bookId.id, isbn, "DDD", "Domain Driven Design", 1000, contractId.id);
    createBook(bookRequest);

    updatePublisherFee(contractId.id, 20L);

    PublisherProjection publisher = getPublisher(contractId.id);

    assertThat(publisher.getFee(), is(2.32));
  }

  @Test
  public void testPlaceOrder() {
    CreateBookRequest randomBook = createRandomBook();
    createBook(randomBook);

    CustomerInformation customer = new CustomerInformation("John Doe", "john@acme.com", "Highway street 1");
    OrderId orderId = addBookToCartAndPlaceOrder(randomBook.bookId, customer);

    OrderProjection order = getOrder(orderId);
    assertThat(order.getOrderId(), is(orderId));
    assertThat(order.getCustomerName(), is(customer.customerName));
    assertThat(order.getStatus(), is(OrderStatus.PLACED));
    assertThat(order.getOrderAmount(), is(1000L));
  }

  @Test
  public void testGetOrders() {
    CreateBookRequest randomBook = createRandomBook();
    createBook(randomBook);

    CustomerInformation customer = new CustomerInformation("John Doe", "john@acme.com", "Highway street 1");

    OrderId orderId1 = addBookToCartAndPlaceOrder(randomBook.bookId, customer);
    OrderId orderId2 = addBookToCartAndPlaceOrder(randomBook.bookId, customer);

    Collection<OrderProjection> orders = getOrders();
    assertThat(orders.size(), is(2));

    Iterator<OrderProjection> iterator = orders.iterator();
    assertThat(iterator.next().getOrderId(), is(orderId2));
    assertThat(iterator.next().getOrderId(), is(orderId1));
  }

  @Test
  public void testActivateOrder() throws Exception {
    CreateBookRequest randomBook = createRandomBook();
    createBook(randomBook);

    CustomerInformation customer = new CustomerInformation("John Doe", "john@acme.com", "Highway street 1");
    OrderId orderId = addBookToCartAndPlaceOrder(randomBook.bookId, customer);

    activateOrder(orderId);

    OrderProjection order = getOrder(orderId);
    assertThat(order.getStatus(), is(ACTIVATED));
  }

  private OrderId addBookToCartAndPlaceOrder(String bookId, CustomerInformation customer) {
    OrderId orderId = OrderId.randomId();
    String cartId = UUID.randomUUID().toString();
    createCart(cartId);
    addItemToCart(cartId, bookId);
    placeOrder(cartId, orderId, customer);
    return orderId;
  }

  private PublisherProjection getPublisher(String publisherId) {
    return client().resource(SERVER_ADDRESS + "/admin/publishers/" + publisherId)
        .accept(APPLICATION_JSON)
        .get(PublisherProjection.class);
  }

  private ClientResponse registerPublisher(String publisherId, String name, double fee) {
    RegisterPublisherRequest request = new RegisterPublisherRequest(publisherId, name, fee);
    ClientResponse response = client().resource(SERVER_ADDRESS + "/admin/register-publisher-requests")
        .entity(request, APPLICATION_JSON)
        .post(ClientResponse.class);
    assertThat(response.getClientResponseStatus().getFamily(), is(SUCCESSFUL));
    return response;
  }

  private ClientResponse updatePublisherFee(String publisherId, long fee) {
    UpdatePublisherFeeRequest request = new UpdatePublisherFeeRequest(publisherId, fee);
    ClientResponse response = client().resource(SERVER_ADDRESS + "/admin/update-publisher-fee-requests")
        .entity(request, APPLICATION_JSON)
        .post(ClientResponse.class);
    assertThat(response.getClientResponseStatus().getFamily(), is(SUCCESSFUL));
    return response;
  }

  private QueryService createQueryService(OrdersPerDayAggregator ordersPerDayAggregator, DomainEventBus domainEventBus) {
    OrderListDenormalizer orderList = new OrderListDenormalizer(new InMemOrderProjectionRepository());
    BookCatalogDenormalizer bookCatalog = new BookCatalogDenormalizer();
    PublisherDenormalizer publisherList = new PublisherDenormalizer();
    OrdersPerDayAggregator ordersPerDay = new OrdersPerDayAggregator();
    QueryService queryService = new QueryService(orderList, bookCatalog, publisherList, ordersPerDay);

    domainEventBus.register(bookCatalog);
    domainEventBus.register(orderList);
    domainEventBus.register(ordersPerDayAggregator);
    domainEventBus.register(publisherList);
    return queryService;
  }

  private ClientResponse updateBookPrice(BookId bookId, long price) {
    UpdateBookPriceRequest request = new UpdateBookPriceRequest(bookId.id, price);
    ClientResponse response = client().resource(SERVER_ADDRESS + "/admin/update-book-price-requests")
        .entity(request, APPLICATION_JSON)
        .post(ClientResponse.class);
    assertThat(response.getClientResponseStatus().getFamily(), is(SUCCESSFUL));
    return response;
  }

  private ClientResponse addItemToCart(String cartId, String bookId) {
    ClientResponse response = client().resource(SERVER_ADDRESS + "/carts/" + cartId + "/items")
        .entity(new BookId(bookId), APPLICATION_JSON)
        .post(ClientResponse.class);
    assertThat(response.getClientResponseStatus().getFamily(), is(SUCCESSFUL));
    return response;
  }

  private void placeOrder(String cartId, OrderId orderId, CustomerInformation customer) {
    placeOrder(new PlaceOrderRequest(cartId, orderId.id, customer.customerName, customer.email, customer.address));
  }

  private ClientResponse activateOrder(OrderId orderId) {
    OrderActivationRequest request = new OrderActivationRequest(orderId.id);
    ClientResponse response = client().resource(SERVER_ADDRESS + "/admin/order-activation-requests")
        .entity(request, APPLICATION_JSON)
        .post(ClientResponse.class);
    assertThat(response.getClientResponseStatus().getFamily(), is(SUCCESSFUL));
    return response;
  }

  private Collection<OrderProjection> getOrders() {
    return client().resource(SERVER_ADDRESS + "/admin/orders")
        .accept(APPLICATION_JSON)
        .get(new GenericType<Collection<OrderProjection>>() {
        });
  }

  private BookProjection getBook(String bookId) {
    return client().resource(SERVER_ADDRESS + "/books/" + bookId)
        .accept(APPLICATION_JSON)
        .get(BookProjection.class);
  }

  private OrderProjection getOrder(OrderId id) {
    return client().resource(SERVER_ADDRESS + "/admin/orders/" + id)
        .accept(APPLICATION_JSON)
        .get(OrderProjection.class);
  }

  private ClientResponse createCart(String cartId) {
    CreateCartRequest createCartRequest = new CreateCartRequest(cartId);
    ClientResponse response = client().resource(SERVER_ADDRESS + "/carts")
        .entity(createCartRequest, APPLICATION_JSON)
        .post(ClientResponse.class);
    assertThat(response.getClientResponseStatus().getFamily(), is(SUCCESSFUL));
    return response;
  }

  private ClientResponse createBook(CreateBookRequest createBookRequest) {
    ClientResponse response = client().resource(SERVER_ADDRESS + "/admin/create-book-requests")
        .entity(createBookRequest, APPLICATION_JSON)
        .post(ClientResponse.class);
    assertThat(response.getClientResponseStatus().getFamily(), is(SUCCESSFUL));
    return response;
  }

  private ClientResponse placeOrder(PlaceOrderRequest orderRequest) {
    ClientResponse response = client().resource(SERVER_ADDRESS + "/order-requests")
        .entity(orderRequest, APPLICATION_JSON)
        .post(ClientResponse.class);
    assertThat(response.getClientResponseStatus().getFamily(), is(SUCCESSFUL));
    return response;
  }

  private CreateBookRequest createRandomBook() {
    BookId bookId = BookId.randomId();
    String isbn = "0321125215";
    return new CreateBookRequest(bookId.id, isbn, "DDD", "Domain Driven Design", 1000, null);
  }

}
