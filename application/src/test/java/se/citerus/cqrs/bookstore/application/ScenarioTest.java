package se.citerus.cqrs.bookstore.application;

import com.google.common.io.Resources;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.GenericType;
import io.dropwizard.testing.junit.DropwizardAppRule;
import org.junit.ClassRule;
import org.junit.Test;
import se.citerus.cqrs.bookstore.admin.web.transport.CreateBookRequest;
import se.citerus.cqrs.bookstore.admin.web.transport.OrderActivationRequest;
import se.citerus.cqrs.bookstore.admin.web.transport.RegisterPublisherRequest;
import se.citerus.cqrs.bookstore.admin.web.transport.UpdateBookPriceRequest;
import se.citerus.cqrs.bookstore.book.BookId;
import se.citerus.cqrs.bookstore.order.CustomerInformation;
import se.citerus.cqrs.bookstore.order.OrderId;
import se.citerus.cqrs.bookstore.order.OrderStatus;
import se.citerus.cqrs.bookstore.publisher.PublisherContractId;
import se.citerus.cqrs.bookstore.query.BookDto;
import se.citerus.cqrs.bookstore.query.OrderProjection;
import se.citerus.cqrs.bookstore.shopping.web.transport.CartDto;
import se.citerus.cqrs.bookstore.shopping.web.transport.CreateCartRequest;
import se.citerus.cqrs.bookstore.shopping.web.transport.LineItemDto;
import se.citerus.cqrs.bookstore.shopping.web.transport.PlaceOrderRequest;

import java.io.File;
import java.util.*;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.Status.Family.SUCCESSFUL;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static se.citerus.cqrs.bookstore.order.OrderStatus.ACTIVATED;

public class ScenarioTest {

  public static final String SERVER_ADDRESS = "http://localhost:8080";

  @ClassRule
  public static final DropwizardAppRule<BookstoreConfiguration> RULE =
      new DropwizardAppRule<>(BookstoreApplication.class, resourceFilePath("test.yml"));

  public static String resourceFilePath(String resourceClassPathLocation) {
    try {
      return new File(Resources.getResource(resourceClassPathLocation).toURI()).getAbsolutePath();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private Client client = new Client();

  @Test
  public void testCreateBook() {
    CreateBookRequest book = createRandomBook();
    createBook(book);
    BookDto bookProjection = getBook(book.bookId);
    assertThat(bookProjection.title, is("DDD"));
    assertThat(bookProjection.description, is("Domain Driven Design"));
    assertThat(bookProjection.isbn, is(book.isbn));
  }

  @Test
  public void testPlaceOrder() throws InterruptedException {
    CreateBookRequest randomBook = createRandomBook();
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
  public void testGetOrders() {
    CreateBookRequest randomBook = createRandomBook();
    createBook(randomBook);
    int initialSize = getOrders().size();

    CustomerInformation customer = new CustomerInformation("John Doe", "john@acme.com", "Highway street 1");

    OrderId orderId1 = addBookToCartAndPlaceOrder(randomBook.bookId, customer);
    OrderId orderId2 = addBookToCartAndPlaceOrder(randomBook.bookId, customer);

    Collection<OrderProjection> orders = getOrders();
    assertThat(orders.size(), is(initialSize + 2));

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

    // TODO: Add await instea of sleep?
    Thread.sleep(500);

    activateOrder(orderId);

    Thread.sleep(500);

    OrderProjection order = getOrder(orderId);
    assertThat(order.getStatus(), is(ACTIVATED));
  }

  private OrderId addBookToCartAndPlaceOrder(String bookId, CustomerInformation customer) {
    OrderId orderId = OrderId.randomId();
    String cartId = UUID.randomUUID().toString();
    createCart(cartId);
    addItemToCart(cartId, bookId);
    placeOrder(cartId, customer);
    return orderId;
  }

  private ClientResponse registerPublisher(String publisherContractId, String name, double feePercentage, long limit) {
    RegisterPublisherRequest request = new RegisterPublisherRequest(publisherContractId, name, feePercentage, limit);
    ClientResponse response = client.resource(SERVER_ADDRESS + "/admin/register-publisher-requests")
        .entity(request, APPLICATION_JSON)
        .post(ClientResponse.class);
    assertThat(response.getStatusInfo().getFamily(), is(SUCCESSFUL));
    return response;
  }


  private ClientResponse updateBookPrice(BookId bookId, long price) {
    UpdateBookPriceRequest request = new UpdateBookPriceRequest(bookId.id, price);
    ClientResponse response = client.resource(SERVER_ADDRESS + "/admin/update-book-price-requests")
        .entity(request, APPLICATION_JSON)
        .post(ClientResponse.class);
    assertThat(response.getStatusInfo().getFamily(), is(SUCCESSFUL));
    return response;
  }

  private ClientResponse addItemToCart(String cartId, String bookId) {
    ClientResponse response = client.resource(SERVER_ADDRESS + "/carts/" + cartId + "/items")
        .entity(new BookId(bookId), APPLICATION_JSON)
        .post(ClientResponse.class);
    assertThat(response.getStatusInfo().getFamily(), is(SUCCESSFUL));
    return response;
  }

  private void placeOrder(String cartId, CustomerInformation customer) {
    PlaceOrderRequest orderRequest = new PlaceOrderRequest();
    orderRequest.orderId = cartId;
    List<LineItemDto> items = new ArrayList<>();
    LineItemDto item = new LineItemDto();
    CreateBookRequest randomBook = createRandomBook();
    item.bookId = randomBook.bookId;
    item.price = randomBook.price;
    item.quantity = 1;
    item.title = randomBook.title;
    items.add(item);
    orderRequest.cart = new CartDto(cartId, items, randomBook.price, 1);
    orderRequest.customerAddress = customer.address;
    orderRequest.customerName = customer.customerName;
    orderRequest.customerEmail = customer.email;


    System.out.println("Placing order: " + orderRequest);
    placeOrder(orderRequest);
  }

  private ClientResponse activateOrder(OrderId orderId) {
    OrderActivationRequest request = new OrderActivationRequest(orderId.id);
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
    CreateCartRequest createCartRequest = new CreateCartRequest(cartId);
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

  private CreateBookRequest createRandomBook() {
    BookId bookId = BookId.randomId();
    String isbn = "0321125215";
    PublisherContractId contractId = PublisherContractId.randomId();
    return new CreateBookRequest(bookId.id, isbn, "DDD", "Domain Driven Design", 1000, contractId.id);
  }

}
