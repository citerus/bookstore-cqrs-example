package se.citerus.cqrs.bookstore.shopping.web;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import io.dropwizard.testing.junit.ResourceTestRule;
import org.junit.After;
import org.junit.ClassRule;
import org.junit.Test;
import se.citerus.cqrs.bookstore.shopping.web.infrastructure.BookClient;
import se.citerus.cqrs.bookstore.shopping.web.infrastructure.InMemoryCartRepository;
import se.citerus.cqrs.bookstore.shopping.web.transport.*;

import java.util.UUID;

import static com.sun.jersey.api.client.ClientResponse.Status.BAD_REQUEST;
import static com.sun.jersey.api.client.ClientResponse.Status.NOT_FOUND;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON_TYPE;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

public class CartResourceTest {

  private static BookClient bookClient = mock(BookClient.class);

  private static final String CART_RESOURCE = "/carts";

  @ClassRule
  public static final ResourceTestRule resources = ResourceTestRule.builder()
      .addResource(new CartResource(bookClient, new InMemoryCartRepository()))
      .build();

  @After
  public void tearDown() throws Exception {
    reset(bookClient);
  }

  @Test
  public void getItemsFromSession() {
    String title = "test title";
    long price = 200L;

    String bookId = UUID.randomUUID().toString();
    String cartId = UUID.randomUUID().toString();
    createCartWithId(cartId, resources.client());

    BookDto book = new BookDto();
    book.price = price;
    book.title = title;
    when(bookClient.getBook(bookId)).thenReturn(book);
    CartDto cart = addItemToCart(cartId, addBookItemRequest(bookId), resources.client()).getEntity(CartDto.class);

    LineItemDto expectedLineItem = new LineItemDto();
    expectedLineItem.bookId = bookId;
    expectedLineItem.title = title;
    expectedLineItem.price = price;
    expectedLineItem.quantity = 1;
    expectedLineItem.totalPrice = price;

    assertThat(cart.lineItems, hasSize(1));
    assertThat(cart.lineItems, hasItems(expectedLineItem));
  }

  @Test
  public void cannotAddNonExistingItem() {
    String cartId = UUID.randomUUID().toString();
    createCartWithId(cartId, resources.client());

    AddItemRequest addItemRequest = addBookItemRequest(UUID.randomUUID().toString());
    ClientResponse response = addItemToCart(cartId, addItemRequest, resources.client());

    assertThat(response.getStatusInfo().getStatusCode(), is(BAD_REQUEST.getStatusCode()));
  }

  private AddItemRequest addBookItemRequest(String bookId) {
    AddItemRequest addItemRequest = new AddItemRequest();
    addItemRequest.bookId = bookId;
    return addItemRequest;
  }

  @Test
  public void cannotGetNonExistingCart() {
    String cartId = UUID.randomUUID().toString();
    ClientResponse response = resources.client()
        .resource(CART_RESOURCE + "/" + cartId)
        .accept(APPLICATION_JSON_TYPE)
        .get(ClientResponse.class);

    assertThat(response.getStatusInfo().getStatusCode(), is(NOT_FOUND.getStatusCode()));
  }

  @Test
  public void shouldSaveCartBetweenCalls() {
    String cartId = UUID.randomUUID().toString();
    createCartWithId(cartId, resources.client());

    CartDto cart = resources.client()
        .resource(CART_RESOURCE + "/" + cartId)
        .accept(APPLICATION_JSON_TYPE)
        .get(CartDto.class);

    assertThat(cart.cartId, is(cartId));
    assertThat(cart.totalPrice, is(0L));
  }

  @Test
  public void shouldClearCart() {
    String cartId = UUID.randomUUID().toString();
    createCartWithId(cartId, resources.client());

    resources.client().resource(CART_RESOURCE + "/" + cartId).delete();

    createCartWithId(cartId, resources.client());
  }

  public static ClientResponse addItemToCart(String cartId, AddItemRequest addItemRequest, Client client) {
    return client
        .resource(CART_RESOURCE + "/" + cartId + "/items")
        .entity(addItemRequest, APPLICATION_JSON_TYPE)
        .post(ClientResponse.class);
  }

  public static void createCartWithId(String cartId, Client client) {
    CreateCartRequest createCart = new CreateCartRequest();
    createCart.cartId = cartId;
    client
        .resource(CART_RESOURCE)
        .entity(createCart, APPLICATION_JSON_TYPE)
        .post();
  }
}
