package se.citerus.cqrs.bookstore.ordercontext.resource;

import io.dropwizard.testing.junit.ResourceTestRule;
import org.junit.After;
import org.junit.ClassRule;
import org.junit.Test;
import se.citerus.cqrs.bookstore.command.CommandBus;
import se.citerus.cqrs.bookstore.ordercontext.api.CartDto;
import se.citerus.cqrs.bookstore.ordercontext.api.LineItemDto;
import se.citerus.cqrs.bookstore.ordercontext.api.PlaceOrderRequest;
import se.citerus.cqrs.bookstore.ordercontext.query.QueryService;

import javax.ws.rs.client.Entity;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;

public class OrderResourceTest {

  private static final String ORDER_RESOURCE = "/order-requests";

  private static final CommandBus commandBus = mock(CommandBus.class);
  private static final QueryService queryService = mock(QueryService.class);

  @ClassRule
  public static final ResourceTestRule resources = ResourceTestRule.builder()
      .addResource(new OrderResource(commandBus))
      .build();

  @After
  public void tearDown() throws Exception {
    reset(queryService, commandBus);
  }

  @Test
  public void testCreateOrderRequest() {
    PlaceOrderRequest newOrderRequest = new PlaceOrderRequest();
    newOrderRequest.orderId = UUID.randomUUID().toString();
    newOrderRequest.customerAddress = "Address";
    newOrderRequest.customerEmail = "test@example.com";
    newOrderRequest.customerName = "John Doe";
    newOrderRequest.cart = createCart();
    createOrder(newOrderRequest);
  }

  private CartDto createCart() {
    CartDto cartDto = new CartDto();
    cartDto.cartId = UUID.randomUUID().toString();
    cartDto.totalPrice = 1200;
    cartDto.totalQuantity = 10;
    cartDto.lineItems = randomLineItems();
    return cartDto;
  }

  private List<LineItemDto> randomLineItems() {
    ArrayList<LineItemDto> lineItems = new ArrayList<>();
    LineItemDto item = new LineItemDto();
    item.productId = UUID.randomUUID().toString();
    item.price = 120;
    item.quantity = 10;
    item.title = "Some book";
    item.totalPrice = 1200;
    lineItems.add(item);
    return lineItems;
  }

  private void createOrder(PlaceOrderRequest newOrderRequest) {
    resources.client().target(ORDER_RESOURCE).request().post(Entity.json(newOrderRequest));
  }
}
