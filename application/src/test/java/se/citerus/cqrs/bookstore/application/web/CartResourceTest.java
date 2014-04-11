package se.citerus.cqrs.bookstore.application.web;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.yammer.dropwizard.testing.ResourceTest;
import org.junit.Test;
import se.citerus.cqrs.bookstore.application.web.transport.CartDto;
import se.citerus.cqrs.bookstore.application.web.transport.CreateCartRequest;
import se.citerus.cqrs.bookstore.application.web.transport.LineItemDto;
import se.citerus.cqrs.bookstore.book.BookId;
import se.citerus.cqrs.bookstore.infrastructure.CartRepository;
import se.citerus.cqrs.bookstore.publisher.PublisherId;
import se.citerus.cqrs.bookstore.query.BookProjection;
import se.citerus.cqrs.bookstore.query.QueryService;

import java.util.UUID;

import static com.sun.jersey.api.client.ClientResponse.Status.BAD_REQUEST;
import static com.sun.jersey.api.client.ClientResponse.Status.NOT_FOUND;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON_TYPE;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CartResourceTest extends ResourceTest {

  private QueryService queryService = mock(QueryService.class);

  private static final String SERVICE_ADDRESS = "http://localhost:8080";
  private static final String CART_RESOURCE = SERVICE_ADDRESS + "/carts";

  @Override
  protected void setUpResources() {
    addResource(new CartResource(queryService, new CartRepository()));
  }

  @Test
  public void getItemsFromSession() {
    String title = "test title";
    long price = 200L;
    BookId bookId = BookId.randomId();
    String isbn = "1234567890";
    PublisherId publisher = PublisherId.randomId();
    String description = "Book description";

    String cartId = UUID.randomUUID().toString();
    createCartWithId(cartId, client());

    BookProjection book = new BookProjection(bookId.id, isbn, title, description, price, publisher.id);
    when(queryService.findBookById(bookId)).thenReturn(book);

    CartDto cart = addItemToCart(cartId, new BookId(bookId.id), client()).getEntity(CartDto.class);

    LineItemDto expectedLineItem = new LineItemDto(bookId.id, title, price, 1, price);
    assertThat(cart.lineItems, hasSize(1));
    assertThat(cart.lineItems, hasItems(expectedLineItem));
  }

  @Test
  public void cannotAddNonExistingItem() {
    BookId bookId = BookId.randomId();
    String cartId = UUID.randomUUID().toString();
    createCartWithId(cartId, client());

    ClientResponse response = addItemToCart(cartId, new BookId(bookId.id), client());

    assertThat(response.getClientResponseStatus(), is(BAD_REQUEST));
  }

  @Test
  public void cannotGetNonExistingCart() {
    String cartId = UUID.randomUUID().toString();
    ClientResponse response = client()
        .resource(CART_RESOURCE + "/" + cartId)
        .accept(APPLICATION_JSON_TYPE)
        .get(ClientResponse.class);

    assertThat(response.getClientResponseStatus(), is(NOT_FOUND));
  }

  @Test
  public void shouldSaveCartBetweenCalls() {
    String cartId = UUID.randomUUID().toString();
    createCartWithId(cartId, client());

    CartDto cart = client()
        .resource(CART_RESOURCE + "/" + cartId)
        .accept(APPLICATION_JSON_TYPE)
        .get(CartDto.class);

    assertThat(cart.cartId, is(cartId));
    assertThat(cart.totalPrice, is(0L));
  }

  @Test
  public void shouldClearCart() {
    String cartId = UUID.randomUUID().toString();
    createCartWithId(cartId, client());

    client().resource(CART_RESOURCE + "/" + cartId).delete();

    createCartWithId(cartId, client());
  }

  public static ClientResponse addItemToCart(String cartId, BookId bookId, Client client) {
    return client
        .resource(CART_RESOURCE + "/" + cartId + "/items")
        .entity(bookId, APPLICATION_JSON_TYPE)
        .post(ClientResponse.class);
  }

  public static void createCartWithId(String cartId, Client client) {
    CreateCartRequest createCart = new CreateCartRequest(cartId);
    client
        .resource(CART_RESOURCE)
        .entity(createCart, APPLICATION_JSON_TYPE)
        .post();
  }
}
