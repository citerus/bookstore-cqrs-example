package se.citerus.cqrs.bookstore.application.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.citerus.cqrs.bookstore.application.web.model.Cart;
import se.citerus.cqrs.bookstore.application.web.model.Item;
import se.citerus.cqrs.bookstore.application.web.transport.CartDto;
import se.citerus.cqrs.bookstore.application.web.transport.CreateCartRequest;
import se.citerus.cqrs.bookstore.book.BookId;
import se.citerus.cqrs.bookstore.infrastructure.CartRepository;
import se.citerus.cqrs.bookstore.query.BookProjection;
import se.citerus.cqrs.bookstore.query.QueryService;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static com.google.common.base.Preconditions.checkArgument;
import static java.lang.String.format;
import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;
import static se.citerus.cqrs.bookstore.GenericId.isValid;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("carts")
public class CartResource {

  private final Logger logger = LoggerFactory.getLogger(getClass());
  private final QueryService queryService;
  private final CartRepository cartRepository;

  public CartResource(QueryService queryService, CartRepository cartRepository) {
    this.queryService = queryService;
    this.cartRepository = cartRepository;
  }

  @POST
  public void initCart(@Valid CreateCartRequest cart) {
    checkArgument(isValid(cart.cartId), "Invalid ID!");
    cartRepository.save(new Cart(cart.cartId));
  }

  @POST
  @Path("{cartId}/items")
  public CartDto addItem(@PathParam("cartId") String cartId, BookId bookId) {
    Cart cart = cartRepository.get(cartId);
    logger.debug("Got addItem request " + bookId);
    BookProjection book = queryService.findBookById(bookId);
    if (book == null) {
      throw new WebApplicationException(Response.status(BAD_REQUEST)
          .entity("Book with id '" + bookId + "' could not be found").build());
    }
    Item item = new Item(bookId, book.getTitle(), book.getPrice());
    logger.info("Adding item to cart: " + item);
    cart.add(item);
    return CartDto.fromCart(cart);
  }

  @GET
  @Path("{cartId}")
  public Response getCart(@PathParam("cartId") String cartId) {
    Cart cart = cartRepository.find(cartId);
    if (cart == null) {
      return Response.status(NOT_FOUND).entity(format("Cart with id '%s' does not exist", cartId)).build();
    } else {
      logger.info("Returning cart with [{}] lines", cart.getLineCount());
      return Response.ok().entity(CartDto.fromCart(cart)).build();
    }
  }

  @DELETE
  @Path("{cartId}")
  public void deleteCart(@PathParam("cartId") String cartId) {
    cartRepository.delete(cartId);
    logger.info("Shopping cart for session '{}' cleared", cartId);
  }

}
