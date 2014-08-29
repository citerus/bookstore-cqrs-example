package se.citerus.cqrs.bookstore.order.resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.citerus.cqrs.bookstore.command.CommandBus;
import se.citerus.cqrs.bookstore.order.CommandFactory;
import se.citerus.cqrs.bookstore.order.api.RegisterPublisherContractRequest;
import se.citerus.cqrs.bookstore.order.publisher.command.RegisterPublisherContractCommand;
import se.citerus.cqrs.bookstore.publisher.PublisherContractId;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@Path("publisher-contract-requests")
@Produces(APPLICATION_JSON)
@Consumes(APPLICATION_JSON)

public class PublisherContractResource {

  private final Logger logger = LoggerFactory.getLogger(getClass());
  private final CommandBus commandBus;
  private final CommandFactory commandFactory = new CommandFactory();

  public PublisherContractResource(CommandBus commandBus) {
    this.commandBus = commandBus;
  }

  @POST
  @Path("register")
  public void registerPublisher(@Valid RegisterPublisherContractRequest request) {
    PublisherContractId publisherContractId = new PublisherContractId(request.publisherContractId);
    logger.info("Registering publisher: " + publisherContractId);
    RegisterPublisherContractCommand command = commandFactory.toCommand(publisherContractId, request);
    commandBus.dispatch(command);
  }

}
