package se.citerus.cqrs.bookstore.shopping.resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.citerus.cqrs.bookstore.shopping.api.AddItemRequest;
import se.citerus.cqrs.bookstore.shopping.api.CartDto;
import se.citerus.cqrs.bookstore.shopping.api.CartDtoFactory;
import se.citerus.cqrs.bookstore.shopping.api.CreateCartRequest;
import se.citerus.cqrs.bookstore.shopping.client.productcatalog.ProductCatalogClient;
import se.citerus.cqrs.bookstore.shopping.client.productcatalog.ProductDto;
import se.citerus.cqrs.bookstore.shopping.domain.Cart;
import se.citerus.cqrs.bookstore.shopping.domain.CartRepository;
import se.citerus.cqrs.bookstore.shopping.domain.Item;
import se.citerus.cqrs.bookstore.shopping.domain.ProductId;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;

import static java.lang.String.format;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;
import static javax.ws.rs.core.Response.status;

@Path("carts")
@Produces(APPLICATION_JSON)
@Consumes(APPLICATION_JSON)
public class CartResource {

  private final Logger logger = LoggerFactory.getLogger(getClass());
  private final ProductCatalogClient productCatalogClient;
  private final CartRepository cartRepository;

  public CartResource(ProductCatalogClient productCatalogClient, CartRepository cartRepository) {
    this.productCatalogClient = productCatalogClient;
    this.cartRepository = cartRepository;
  }

  @POST
  public void initCart(@Valid CreateCartRequest cart) {
    cartRepository.save(new Cart(cart.cartId));
  }

  @POST
  @Path("{cartId}/items")
  public CartDto addItem(@PathParam("cartId") String cartId, AddItemRequest addItemRequest) {
    Cart cart = cartRepository.get(cartId);
    logger.debug("Got addItem request " + addItemRequest);
    ProductDto product = productCatalogClient.getProduct(addItemRequest.productId);
    assertProductExists(addItemRequest.productId, product);
    Item item = new Item(new ProductId(addItemRequest.productId), product.book.title, product.price);
    logger.info("Adding item to cart: " + item);
    cart.add(item);
    return CartDtoFactory.fromCart(cart);
  }

  @GET
  @Path("{cartId}")
  public Response getCart(@PathParam("cartId") String cartId) {
    Cart cart = cartRepository.find(cartId);
    if (cart == null) {
      return status(NOT_FOUND).entity(format("Cart with id '%s' does not exist", cartId)).build();
    } else {
      logger.info("Returning cart with [{}] lines", cart.getLineCount());
      return Response.ok().entity(CartDtoFactory.fromCart(cart)).build();
    }
  }

  @DELETE
  @Path("{cartId}")
  public void deleteCart(@PathParam("cartId") String cartId) {
    cartRepository.delete(cartId);
    logger.info("Shopping cart for session [{}] cleared", cartId);
  }

  private void assertProductExists(String productId, ProductDto product) {
    if (product == null) {
      throw new WebApplicationException(status(BAD_REQUEST)
          .entity("Product with id '" + productId + "' could not be found").build());
    }
  }

}
