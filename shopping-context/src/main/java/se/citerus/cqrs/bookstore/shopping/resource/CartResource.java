package se.citerus.cqrs.bookstore.shopping.resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.citerus.cqrs.bookstore.shopping.api.AddItemRequest;
import se.citerus.cqrs.bookstore.shopping.api.CartDto;
import se.citerus.cqrs.bookstore.shopping.api.CartDtoFactory;
import se.citerus.cqrs.bookstore.shopping.api.CreateCartRequest;
import se.citerus.cqrs.bookstore.shopping.client.bookcatalog.BookClient;
import se.citerus.cqrs.bookstore.shopping.client.bookcatalog.BookDto;
import se.citerus.cqrs.bookstore.shopping.model.BookId;
import se.citerus.cqrs.bookstore.shopping.model.Cart;
import se.citerus.cqrs.bookstore.shopping.model.CartRepository;
import se.citerus.cqrs.bookstore.shopping.model.Item;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static java.lang.String.format;
import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;
import static javax.ws.rs.core.Response.status;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("carts")
public class CartResource {

  private final Logger logger = LoggerFactory.getLogger(getClass());
  private final BookClient bookClient;
  private final CartRepository cartRepository;

  public CartResource(BookClient bookClient, CartRepository cartRepository) {
    this.bookClient = bookClient;
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
    BookDto book = bookClient.getBook(addItemRequest.bookId);
    assertBookExists(addItemRequest.bookId, book);
    Item item = new Item(new BookId(addItemRequest.bookId), book.title, book.price);
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
    logger.info("Shopping cart for session '{}' cleared", cartId);
  }

  private void assertBookExists(String bookId, BookDto book) {
    if (book == null) {
      throw new WebApplicationException(status(BAD_REQUEST)
          .entity("Book with id '" + bookId + "' could not be found").build());
    }
  }

}
