package se.citerus.cqrs.bookstore.application.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.citerus.cqrs.bookstore.application.CommandFactory;
import se.citerus.cqrs.bookstore.application.web.model.Cart;
import se.citerus.cqrs.bookstore.application.web.transport.PlaceOrderRequest;
import se.citerus.cqrs.bookstore.command.CommandBus;
import se.citerus.cqrs.bookstore.command.order.PlaceOrderCommand;
import se.citerus.cqrs.bookstore.infrastructure.CartRepository;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@Path("order-requests")
@Produces(APPLICATION_JSON)
@Consumes(APPLICATION_JSON)
public class OrderResource {

  private final Logger logger = LoggerFactory.getLogger(getClass());
  private final CartRepository cartRepository;
  private final CommandBus commandBus;
  private final CommandFactory commandFactory = new CommandFactory();

  public OrderResource(CommandBus commandBus, CartRepository cartRepository) {
    this.commandBus = commandBus;
    this.cartRepository = cartRepository;
  }

  @POST
  public void placeOrder(@Valid PlaceOrderRequest placeOrderRequest) {
    logger.info("Placing customer order: " + placeOrderRequest);
    Cart cart = cartRepository.get(placeOrderRequest.cartId);
    PlaceOrderCommand placeOrderCommand = commandFactory.toCommand(cart, placeOrderRequest);
    commandBus.dispatch(placeOrderCommand);
    cartRepository.delete(cart.cartId);
  }

}
