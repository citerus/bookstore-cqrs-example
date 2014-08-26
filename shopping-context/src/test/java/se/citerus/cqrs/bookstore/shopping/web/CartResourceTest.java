package se.citerus.cqrs.bookstore.shopping.web;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import io.dropwizard.testing.junit.ResourceTestRule;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Test;
import se.citerus.cqrs.bookstore.book.BookId;
import se.citerus.cqrs.bookstore.publisher.PublisherContractId;
import se.citerus.cqrs.bookstore.shopping.web.transport.BookProjection;
import se.citerus.cqrs.bookstore.shopping.web.transport.CartDto;
import se.citerus.cqrs.bookstore.shopping.web.transport.CreateCartRequest;
import se.citerus.cqrs.bookstore.shopping.web.transport.LineItemDto;

import java.util.UUID;

import static com.sun.jersey.api.client.ClientResponse.Status.BAD_REQUEST;
import static com.sun.jersey.api.client.ClientResponse.Status.NOT_FOUND;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON_TYPE;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.*;

public class CartResourceTest {

  private static BookClient bookClient = mock(BookClient.class);

  private static final String SERVICE_ADDRESS = "http://localhost:8080";
  private static final String CART_RESOURCE = SERVICE_ADDRESS + "/carts";


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
    BookId bookId = BookId.randomId();
    String isbn = "1234567890";
    PublisherContractId publisher = PublisherContractId.randomId();
    String description = "Book description";

    String cartId = UUID.randomUUID().toString();
    createCartWithId(cartId, resources.client());

    BookProjection book = new BookProjection();
    book.price = price;
    book.title = title;
    when(bookClient.getBook(bookId.id)).thenReturn(book);

    CartDto cart = addItemToCart(cartId, new BookId(bookId.id), resources.client()).getEntity(CartDto.class);

    LineItemDto expectedLineItem = new LineItemDto(bookId.id, title, price, 1, price);
    Assert.assertThat(cart.lineItems, Matchers.hasSize(1));
    Assert.assertThat(cart.lineItems, Matchers.hasItems(expectedLineItem));
  }

  @Test
  public void cannotAddNonExistingItem() {
    BookId bookId = BookId.randomId();
    String cartId = UUID.randomUUID().toString();
    createCartWithId(cartId, resources.client());

    ClientResponse response = addItemToCart(cartId, new BookId(bookId.id), resources.client());

    Assert.assertThat(response.getStatusInfo().getStatusCode(), is(BAD_REQUEST.getStatusCode()));
  }

  @Test
  public void cannotGetNonExistingCart() {
    String cartId = UUID.randomUUID().toString();
    ClientResponse response = resources.client()
        .resource(CART_RESOURCE + "/" + cartId)
        .accept(APPLICATION_JSON_TYPE)
        .get(ClientResponse.class);

    Assert.assertThat(response.getClientResponseStatus(), is(NOT_FOUND));
  }

  @Test
  public void shouldSaveCartBetweenCalls() {
    String cartId = UUID.randomUUID().toString();
    createCartWithId(cartId, resources.client());

    CartDto cart = resources.client()
        .resource(CART_RESOURCE + "/" + cartId)
        .accept(APPLICATION_JSON_TYPE)
        .get(CartDto.class);

    Assert.assertThat(cart.cartId, is(cartId));
    Assert.assertThat(cart.totalPrice, is(0L));
  }

  @Test
  public void shouldClearCart() {
    String cartId = UUID.randomUUID().toString();
    createCartWithId(cartId, resources.client());

    resources.client().resource(CART_RESOURCE + "/" + cartId).delete();

    createCartWithId(cartId, resources.client());
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
