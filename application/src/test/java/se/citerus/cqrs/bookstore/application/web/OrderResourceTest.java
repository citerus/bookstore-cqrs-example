package se.citerus.cqrs.bookstore.application.web;

import com.yammer.dropwizard.testing.ResourceTest;
import org.junit.Test;
import se.citerus.cqrs.bookstore.application.web.transport.PlaceOrderRequest;
import se.citerus.cqrs.bookstore.book.BookId;
import se.citerus.cqrs.bookstore.command.CommandBus;
import se.citerus.cqrs.bookstore.infrastructure.CartRepository;
import se.citerus.cqrs.bookstore.query.BookProjection;
import se.citerus.cqrs.bookstore.query.QueryService;

import java.util.UUID;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON_TYPE;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static se.citerus.cqrs.bookstore.application.web.CartResourceTest.addItemToCart;
import static se.citerus.cqrs.bookstore.application.web.CartResourceTest.createCartWithId;

public class OrderResourceTest extends ResourceTest {

  private static final String SERVICE_ADDRESS = "http://localhost:8080";
  private static final String ORDER_RESOURCE = SERVICE_ADDRESS + "/order-requests";

  private final CommandBus commandBus = mock(CommandBus.class);
  private final QueryService queryService = mock(QueryService.class);
  private final CartRepository cartRepository = new CartRepository();

  @Override
  protected void setUpResources() {
    addResource(new CartResource(queryService, cartRepository));
    addResource(new OrderResource(commandBus, cartRepository));
  }

  @Test
  public void testCreateOrderRequest() {
    String cartId = UUID.randomUUID().toString();
    String bookId = UUID.randomUUID().toString();
    String orderId = UUID.randomUUID().toString();
    PlaceOrderRequest newOrderRequest = new PlaceOrderRequest(cartId, orderId, "TestCustomer", "test@example.com", "Street 1");

    BookProjection book = mock(BookProjection.class);
    when(queryService.findBookById(new BookId(bookId))).thenReturn(book);

    createCartWithId(cartId, client());
    addItemToCart(cartId, new BookId(bookId), client());
    createOrder(newOrderRequest);
  }

  private void createOrder(PlaceOrderRequest newOrderRequest) {
    client().resource(ORDER_RESOURCE).entity(newOrderRequest, APPLICATION_JSON_TYPE).post();
  }
}
