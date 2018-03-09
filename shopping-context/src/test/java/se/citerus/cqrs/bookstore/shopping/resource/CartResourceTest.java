package se.citerus.cqrs.bookstore.shopping.resource;

import io.dropwizard.testing.junit.ResourceTestRule;
import org.junit.After;
import org.junit.ClassRule;
import org.junit.Test;
import se.citerus.cqrs.bookstore.shopping.api.AddItemRequest;
import se.citerus.cqrs.bookstore.shopping.api.CartDto;
import se.citerus.cqrs.bookstore.shopping.api.CreateCartRequest;
import se.citerus.cqrs.bookstore.shopping.api.LineItemDto;
import se.citerus.cqrs.bookstore.shopping.client.productcatalog.BookDto;
import se.citerus.cqrs.bookstore.shopping.client.productcatalog.ProductCatalogClient;
import se.citerus.cqrs.bookstore.shopping.client.productcatalog.ProductDto;
import se.citerus.cqrs.bookstore.shopping.infrastructure.InMemoryCartRepository;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import java.util.UUID;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON_TYPE;
import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

public class CartResourceTest {

  private static ProductCatalogClient productCatalogClient = mock(ProductCatalogClient.class);

  private static final String CART_RESOURCE = "/carts";

  @ClassRule
  public static final ResourceTestRule resources = ResourceTestRule.builder()
      .addResource(new CartResource(productCatalogClient, new InMemoryCartRepository()))
      .build();

  @After
  public void tearDown() throws Exception {
    reset(productCatalogClient);
  }

  @Test
  public void getItemsFromSession() {
    String title = "test title";
    long price = 200L;

    String productId = UUID.randomUUID().toString();
    String cartId = UUID.randomUUID().toString();
    createCartWithId(cartId);

    ProductDto productDto = new ProductDto();
    productDto.price = price;
    productDto.book = new BookDto();
    productDto.book.title = title;
    when(productCatalogClient.getProduct(productId)).thenReturn(productDto);
    CartDto cart = addItemToCart(cartId, addProductItemRequest(productId)).readEntity(CartDto.class);

    LineItemDto expectedLineItem = new LineItemDto();
    expectedLineItem.productId = productId;
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
    createCartWithId(cartId);

    AddItemRequest addItemRequest = addProductItemRequest(UUID.randomUUID().toString());
    Response response = addItemToCart(cartId, addItemRequest);

    assertThat(response.getStatusInfo().getStatusCode(), is(BAD_REQUEST.getStatusCode()));
  }

  private AddItemRequest addProductItemRequest(String productId) {
    AddItemRequest addItemRequest = new AddItemRequest();
    addItemRequest.productId = productId;
    return addItemRequest;
  }

  @Test
  public void cannotGetNonExistingCart() {
    String cartId = UUID.randomUUID().toString();
    Response response = resources.client()
        .target(CART_RESOURCE + "/" + cartId)
        .request(APPLICATION_JSON_TYPE)
        .get(Response.class);

    assertThat(response.getStatusInfo().getStatusCode(), is(NOT_FOUND.getStatusCode()));
  }

  @Test
  public void shouldSaveCartBetweenCalls() {
    String cartId = UUID.randomUUID().toString();
    createCartWithId(cartId);

    CartDto cart = resources.client()
        .target(CART_RESOURCE + "/" + cartId)
        .request(APPLICATION_JSON_TYPE)
        .get(CartDto.class);

    assertThat(cart.cartId, is(cartId));
    assertThat(cart.totalPrice, is(0L));
  }

  @Test
  public void shouldClearCart() {
    String cartId = UUID.randomUUID().toString();
    createCartWithId(cartId);

    resources.client().target(CART_RESOURCE + "/" + cartId).request().delete();

    createCartWithId(cartId);
  }

  private Response addItemToCart(String cartId, AddItemRequest addItemRequest) {
    return resources.client()
        .target(CART_RESOURCE + "/" + cartId + "/items")
        .request()
        .post(Entity.json(addItemRequest), Response.class);
  }

  private void createCartWithId(String cartId) {
    CreateCartRequest createCart = new CreateCartRequest();
    createCart.cartId = cartId;
    resources.client()
        .target(CART_RESOURCE)
        .request()
        .post(Entity.json(createCart));
  }

}
