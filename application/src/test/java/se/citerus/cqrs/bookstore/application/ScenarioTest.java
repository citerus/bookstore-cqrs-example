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
import se.citerus.cqrs.bookstore.ordercontext.api.CartDto;
import se.citerus.cqrs.bookstore.ordercontext.api.OrderActivationRequest;
import se.citerus.cqrs.bookstore.ordercontext.api.PlaceOrderRequest;
import se.citerus.cqrs.bookstore.ordercontext.api.RegisterPublisherContractRequest;
import se.citerus.cqrs.bookstore.ordercontext.client.productcatalog.BookDto;
import se.citerus.cqrs.bookstore.ordercontext.client.productcatalog.ProductDto;
import se.citerus.cqrs.bookstore.ordercontext.order.*;
import se.citerus.cqrs.bookstore.ordercontext.query.orderlist.OrderProjection;
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
    ProductDto product = createRandomProduct(UUID.randomUUID().toString());
    createProduct(product);
    ProductDto bookProjection = getProduct(product.productId);
    assertThat(bookProjection.book.title, is("DDD"));
    assertThat(bookProjection.book.description, is("Domain Driven Design"));
    assertThat(bookProjection.book.isbn, is(product.book.isbn));
  }

  @Test
  public void testPlaceOrder() throws InterruptedException {
    ProductDto product = createRandomProduct(UUID.randomUUID().toString());
    createProduct(product);

    CustomerInformation customer = new CustomerInformation("John Doe", "john@acme.com", "Highway street 1");
    OrderId orderId = addBookToCartAndPlaceOrder(product.productId, customer);

    Thread.sleep(500);

    OrderProjection order = getOrder(orderId);
    assertThat(order.getOrderId(), is(orderId));
    assertThat(order.getCustomerName(), is(customer.customerName));
    assertThat(order.getStatus(), is(OrderStatus.PLACED));
    assertThat(order.getOrderAmount(), is(1000L));
  }

  @Test
  public void testGetOrders() throws InterruptedException {
    ProductDto product = createRandomProduct(UUID.randomUUID().toString());
    createProduct(product);
    int initialSize = getOrders().size();

    CustomerInformation customer = new CustomerInformation("John Doe", "john@acme.com", "Highway street 1");

    OrderId orderId1 = addBookToCartAndPlaceOrder(product.productId, customer);
    OrderId orderId2 = addBookToCartAndPlaceOrder(product.productId, customer);

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

    ProductDto product = createRandomProduct(publisherContractId);
    createProduct(product);

    CustomerInformation customer = new CustomerInformation("John Doe", "john@acme.com", "Highway street 1");
    OrderId orderId = addBookToCartAndPlaceOrder(product.productId, customer);

    // TODO: Add await instead of sleep?
    Thread.sleep(200);

    activateOrder(orderId);

    Thread.sleep(200);

    OrderProjection order = getOrder(orderId);
    assertThat(order.getStatus(), is(ACTIVATED));
  }

  private OrderId addBookToCartAndPlaceOrder(String productId, CustomerInformation customer) throws InterruptedException {
    String cartId = UUID.randomUUID().toString();
    Thread.sleep(200);
    createCart(cartId);
    Thread.sleep(200);
    addItemToCart(cartId, productId);
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
    ClientResponse response = client.resource(SERVER_ADDRESS + "/publishercontract-requests")
        .entity(request, APPLICATION_JSON)
        .post(ClientResponse.class);
    assertThat(response.getStatusInfo().getFamily(), is(SUCCESSFUL));
    return response;
  }

  private ClientResponse addItemToCart(String cartId, String productId) {
    AddItemRequest addItemRequest = new AddItemRequest();
    addItemRequest.productId = productId;

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
    ClientResponse response = client.resource(SERVER_ADDRESS + "/order-requests/activations")
        .entity(request, APPLICATION_JSON)
        .post(ClientResponse.class);
    assertThat(response.getStatusInfo().getFamily(), is(SUCCESSFUL));
    return response;
  }

  private Collection<OrderProjection> getOrders() {
    return client.resource(SERVER_ADDRESS + "/query/orders")
        .accept(APPLICATION_JSON)
        .get(new GenericType<Collection<OrderProjection>>() {
        });
  }

  private ProductDto getProduct(String productId) {
    return client.resource(SERVER_ADDRESS + "/products/" + productId)
        .accept(APPLICATION_JSON)
        .get(ProductDto.class);
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

  private ClientResponse createProduct(ProductDto productRequest) {
    ClientResponse response = client.resource(SERVER_ADDRESS + "/products")
        .entity(productRequest, APPLICATION_JSON)
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

  private ProductDto createRandomProduct(String publisherContractId) {
    BookId bookId = BookId.randomId();
    BookDto bookDto = new BookDto();
    bookDto.bookId = bookId.id;
    bookDto.isbn = "0321125215";
    bookDto.title = "DDD";
    bookDto.description = "Domain Driven Design";

    ProductDto productDto = new ProductDto();
    productDto.productId = ProductId.randomId().id;
    productDto.book = bookDto;
    productDto.price = 1000;
    productDto.publisherContractId = publisherContractId;
    return productDto;
  }

}
