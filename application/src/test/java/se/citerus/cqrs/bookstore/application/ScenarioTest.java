package se.citerus.cqrs.bookstore.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.Resources;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.GenericType;
import io.dropwizard.testing.junit.DropwizardAppRule;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import se.citerus.cqrs.bookstore.admin.api.CreateBookRequest;
import se.citerus.cqrs.bookstore.admin.api.OrderActivationRequest;
import se.citerus.cqrs.bookstore.admin.api.RegisterPublisherContractRequest;
import se.citerus.cqrs.bookstore.ordercontext.order.BookId;
import se.citerus.cqrs.bookstore.ordercontext.order.CustomerInformation;
import se.citerus.cqrs.bookstore.ordercontext.order.OrderId;
import se.citerus.cqrs.bookstore.ordercontext.order.OrderStatus;
import se.citerus.cqrs.bookstore.ordercontext.order.api.CartDto;
import se.citerus.cqrs.bookstore.ordercontext.order.api.PlaceOrderRequest;
import se.citerus.cqrs.bookstore.query.client.bookcatalog.BookDto;
import se.citerus.cqrs.bookstore.query.orderlist.OrderProjection;
import se.citerus.cqrs.bookstore.shopping.api.AddItemRequest;
import se.citerus.cqrs.bookstore.shopping.api.CreateCartRequest;

import java.io.File;
import java.util.Collection;
import java.util.Iterator;
import java.util.UUID;

import static com.fasterxml.jackson.databind.SerializationFeature.INDENT_OUTPUT;
import static com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.Status.Family.SUCCESSFUL;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static se.citerus.cqrs.bookstore.ordercontext.order.OrderStatus.ACTIVATED;

public class ScenarioTest {

  public static final String SERVER_ADDRESS = "http://localhost:8080/service";

  @ClassRule
  public static final DropwizardAppRule<BookstoreConfiguration> RULE =
      new DropwizardAppRule<>(BookstoreApplication.class, resourceFilePath("test.yml"));

  private Client client = new Client();

  public static String resourceFilePath(String resourceClassPathLocation) {
    try {
      return new File(Resources.getResource(resourceClassPathLocation).toURI()).getAbsolutePath();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Before
  public void setUp() throws Exception {
    ObjectMapper objectMapper = RULE.getEnvironment().getObjectMapper();
    objectMapper.enable(INDENT_OUTPUT);
    objectMapper.enable(WRITE_DATES_AS_TIMESTAMPS);
  }

  @Test
  public void testCreateBook() {
    CreateBookRequest book = createRandomBook(UUID.randomUUID().toString());
    createBook(book);
    BookDto bookProjection = getBook(book.bookId);
    assertThat(bookProjection.title, is("DDD"));
    assertThat(bookProjection.description, is("Domain Driven Design"));
    assertThat(bookProjection.isbn, is(book.isbn));
  }

  @Test
  public void testPlaceOrder() throws InterruptedException {
    CreateBookRequest randomBook = createRandomBook(UUID.randomUUID().toString());
    createBook(randomBook);

    CustomerInformation customer = new CustomerInformation("John Doe", "john@acme.com", "Highway street 1");
    OrderId orderId = addBookToCartAndPlaceOrder(randomBook.bookId, customer);

    Thread.sleep(500);

    OrderProjection order = getOrder(orderId);
    assertThat(order.getOrderId(), is(orderId));
    assertThat(order.getCustomerName(), is(customer.customerName));
    assertThat(order.getStatus(), is(OrderStatus.PLACED));
    assertThat(order.getOrderAmount(), is(1000L));
  }

  @Test
  public void testGetOrders() throws InterruptedException {
    CreateBookRequest randomBook = createRandomBook(UUID.randomUUID().toString());
    createBook(randomBook);
    int initialSize = getOrders().size();

    CustomerInformation customer = new CustomerInformation("John Doe", "john@acme.com", "Highway street 1");

    OrderId orderId1 = addBookToCartAndPlaceOrder(randomBook.bookId, customer);
    OrderId orderId2 = addBookToCartAndPlaceOrder(randomBook.bookId, customer);

    Thread.sleep(500);

    Collection<OrderProjection> orders = getOrders();
    assertThat(orders.size(), is(initialSize + 2));

    Iterator<OrderProjection> iterator = orders.iterator();
    assertThat(iterator.next().getOrderId(), is(orderId2));
    assertThat(iterator.next().getOrderId(), is(orderId1));
  }

  @Test
  public void testActivateOrder() throws Exception {
    String publisherContractId = UUID.randomUUID().toString();
    registerPublisher(publisherContractId, "Addison-Wesley", 10.0, 1000);

    CreateBookRequest randomBook = createRandomBook(publisherContractId);
    createBook(randomBook);

    CustomerInformation customer = new CustomerInformation("John Doe", "john@acme.com", "Highway street 1");
    OrderId orderId = addBookToCartAndPlaceOrder(randomBook.bookId, customer);

    // TODO: Add await instea of sleep?
    Thread.sleep(200);

    activateOrder(orderId);

    Thread.sleep(200);

    OrderProjection order = getOrder(orderId);
    assertThat(order.getStatus(), is(ACTIVATED));
  }

  private OrderId addBookToCartAndPlaceOrder(String bookId, CustomerInformation customer) throws InterruptedException {
    String cartId = UUID.randomUUID().toString();
    Thread.sleep(200);
    createCart(cartId);
    Thread.sleep(200);
    addItemToCart(cartId, bookId);
    Thread.sleep(200);
    CartDto cart = getCart(cartId);
    placeOrder(cartId, customer, cart);
    return new OrderId(cartId);
  }

  private CartDto getCart(String cartId) {
    return client.resource(SERVER_ADDRESS + "/carts/" + cartId)
        .get(CartDto.class);
  }

  private ClientResponse registerPublisher(String publisherContractId, String name, double feePercentage, long limit) {
    RegisterPublisherContractRequest request = new RegisterPublisherContractRequest();
    request.publisherContractId = publisherContractId;
    request.publisherName = name;
    request.feePercentage = feePercentage;
    request.limit = limit;
    ClientResponse response = client.resource(SERVER_ADDRESS + "/admin/publishercontract-requests")
        .entity(request, APPLICATION_JSON)
        .post(ClientResponse.class);
    assertThat(response.getStatusInfo().getFamily(), is(SUCCESSFUL));
    return response;
  }

  private ClientResponse addItemToCart(String cartId, String bookId) {
    AddItemRequest addItemRequest = new AddItemRequest();
    addItemRequest.bookId = bookId;

    ClientResponse response = client.resource(SERVER_ADDRESS + "/carts/" + cartId + "/items")
        .entity(addItemRequest, APPLICATION_JSON)
        .post(ClientResponse.class);

    assertThat(response.getStatusInfo().getFamily(), is(SUCCESSFUL));
    return response;
  }

  private void placeOrder(String cartId, CustomerInformation customer, CartDto cart) {
    PlaceOrderRequest orderRequest = new PlaceOrderRequest();
    orderRequest.orderId = cartId;
    orderRequest.cart = cart;
    orderRequest.customerAddress = customer.address;
    orderRequest.customerName = customer.customerName;
    orderRequest.customerEmail = customer.email;

    System.out.println("Placing order: " + orderRequest);
    placeOrder(orderRequest);
  }

  private ClientResponse activateOrder(OrderId orderId) {
    OrderActivationRequest request = new OrderActivationRequest();
    request.orderId = orderId.id;
    ClientResponse response = client.resource(SERVER_ADDRESS + "/admin/order-activation-requests")
        .entity(request, APPLICATION_JSON)
        .post(ClientResponse.class);
    assertThat(response.getStatusInfo().getFamily(), is(SUCCESSFUL));
    return response;
  }

  private Collection<OrderProjection> getOrders() {
    return client.resource(SERVER_ADDRESS + "/admin/orders")
        .accept(APPLICATION_JSON)
        .get(new GenericType<Collection<OrderProjection>>() {
        });
  }

  private BookDto getBook(String bookId) {
    return client.resource(SERVER_ADDRESS + "/books/" + bookId)
        .accept(APPLICATION_JSON)
        .get(BookDto.class);
  }

  private OrderProjection getOrder(OrderId id) {
    for (OrderProjection orderProjection : getOrders()) {
      if (orderProjection.getOrderId().equals(id)) return orderProjection;
    }
    throw new IllegalArgumentException("No such order: " + id);
  }

  private ClientResponse createCart(String cartId) {
    CreateCartRequest createCartRequest1 = new CreateCartRequest();
    createCartRequest1.cartId = cartId;
    CreateCartRequest createCartRequest = createCartRequest1;
    ClientResponse response = client.resource(SERVER_ADDRESS + "/carts")
        .entity(createCartRequest, APPLICATION_JSON)
        .post(ClientResponse.class);
    assertThat(response.getStatusInfo().getFamily(), is(SUCCESSFUL));
    return response;
  }

  private ClientResponse createBook(CreateBookRequest createBookRequest) {
    ClientResponse response = client.resource(SERVER_ADDRESS + "/admin/create-book-requests")
        .entity(createBookRequest, APPLICATION_JSON)
        .post(ClientResponse.class);
    assertThat(response.getStatusInfo().getFamily(), is(SUCCESSFUL));
    return response;
  }

  private ClientResponse placeOrder(PlaceOrderRequest orderRequest) {
    ClientResponse response = client.resource(SERVER_ADDRESS + "/order-requests")
        .entity(orderRequest, APPLICATION_JSON)
        .post(ClientResponse.class);

    assertThat(response.getStatusInfo().getFamily(), is(SUCCESSFUL));
    return response;
  }

  private CreateBookRequest createRandomBook(String publisherContractId) {
    BookId bookId = BookId.randomId();
    CreateBookRequest createBookRequest = new CreateBookRequest();
    createBookRequest.bookId = bookId.id;
    createBookRequest.isbn = "0321125215";
    createBookRequest.title = "DDD";
    createBookRequest.description = "Domain Driven Design";
    createBookRequest.price = 1000;
    createBookRequest.publisherContractId = publisherContractId;
    return createBookRequest;
  }

}
