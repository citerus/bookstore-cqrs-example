package se.citerus.cqrs.bookstore.application.web;

import io.dropwizard.testing.junit.ResourceTestRule;
import org.junit.After;
import org.junit.ClassRule;
import org.junit.Test;
import se.citerus.cqrs.bookstore.book.BookId;
import se.citerus.cqrs.bookstore.command.CommandBus;
import se.citerus.cqrs.bookstore.infrastructure.InMemoryCartRepository;
import se.citerus.cqrs.bookstore.query.BookProjection;
import se.citerus.cqrs.bookstore.query.QueryService;
import se.citerus.cqrs.bookstore.shopping.web.CartResource;
import se.citerus.cqrs.bookstore.shopping.web.model.CartRepository;
import se.citerus.cqrs.bookstore.shopping.web.transport.CartDto;
import se.citerus.cqrs.bookstore.shopping.web.transport.LineItemDto;
import se.citerus.cqrs.bookstore.shopping.web.transport.PlaceOrderRequest;

import java.util.ArrayList;
import java.util.UUID;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON_TYPE;
import static org.mockito.Mockito.*;
import static se.citerus.cqrs.bookstore.application.web.CartResourceTest.addItemToCart;
import static se.citerus.cqrs.bookstore.application.web.CartResourceTest.createCartWithId;

public class OrderResourceTest {

  private static final String SERVICE_ADDRESS = "http://localhost:8080";
  private static final String ORDER_RESOURCE = SERVICE_ADDRESS + "/order-requests";

  private static final CommandBus commandBus = mock(CommandBus.class);
  private static final QueryService queryService = mock(QueryService.class);
  private static CartClient cartClient = mock(CartClient.class);
  private static CartRepository cartRepository = new InMemoryCartRepository();


  @ClassRule
  public static final ResourceTestRule resources = ResourceTestRule.builder()
      .addResource(new CartResource(queryService, cartRepository))
      .addResource(new OrderResource(commandBus, cartClient))
      .build();

  @After
  public void tearDown() throws Exception {
    reset(queryService, commandBus, cartClient);
  }


  @Test
  public void testCreateOrderRequest() {
    String cartId = UUID.randomUUID().toString();
    String bookId = UUID.randomUUID().toString();
    String orderId = UUID.randomUUID().toString();
    PlaceOrderRequest newOrderRequest = new PlaceOrderRequest(cartId, orderId, "TestCustomer", "test@example.com", "Street 1");

    BookProjection book = mock(BookProjection.class);
    when(queryService.getBook(bookId)).thenReturn(book);
    ArrayList<LineItemDto> lineItems = new ArrayList<>();
    lineItems.add(new LineItemDto(bookId, "Test", 100, 10, 100));
    CartDto cart = new CartDto(cartId, lineItems, 1000, 100);
    when(cartClient.get(cartId)).thenReturn(cart);

    createCartWithId(cartId, resources.client());
    addItemToCart(cartId, new BookId(bookId), resources.client());
    createOrder(newOrderRequest);
  }

  private void createOrder(PlaceOrderRequest newOrderRequest) {
    resources.client().resource(ORDER_RESOURCE).entity(newOrderRequest, APPLICATION_JSON_TYPE).post();
  }
}
