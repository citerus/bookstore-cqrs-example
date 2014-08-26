package se.citerus.cqrs.bookstore.order.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.citerus.cqrs.bookstore.command.CommandBus;
import se.citerus.cqrs.bookstore.order.command.ActivateOrderCommand;
import se.citerus.cqrs.bookstore.order.command.PlaceOrderCommand;
import se.citerus.cqrs.bookstore.order.web.transport.CartDto;
import se.citerus.cqrs.bookstore.order.web.transport.PlaceOrderRequest;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("order-requests")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class OrderResource {

  private final Logger logger = LoggerFactory.getLogger(getClass());
  private final CartClient cartClient;
  private final CommandBus commandBus;
  private final CommandFactory commandFactory = new CommandFactory();

  public OrderResource(CommandBus commandBus, CartClient cartClient) {
    this.commandBus = commandBus;
    this.cartClient = cartClient;
  }

  @POST
  @Path("activations")
  public void orderActivationRequest(@Valid OrderActivationRequest activationRequest) {
    logger.info("Activating orderId: " + activationRequest.orderId);
    ActivateOrderCommand command = commandFactory.toCommand(activationRequest);
    commandBus.dispatch(command);
  }

  @POST
  public void placeOrder(@Valid PlaceOrderRequest placeOrderRequest) {
    logger.info("Placing customer order: " + placeOrderRequest);
    CartDto cart = placeOrderRequest.cart;
    PlaceOrderCommand placeOrderCommand = commandFactory.toCommand(cart, placeOrderRequest);
    commandBus.dispatch(placeOrderCommand);
    cartClient.delete(cart.cartId);
  }

}
