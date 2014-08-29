package se.citerus.cqrs.bookstore.ordercontext.resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.citerus.cqrs.bookstore.command.CommandBus;
import se.citerus.cqrs.bookstore.ordercontext.api.RegisterPublisherContractRequest;
import se.citerus.cqrs.bookstore.ordercontext.publishercontract.PublisherContractId;
import se.citerus.cqrs.bookstore.ordercontext.publishercontract.command.CommandFactory;
import se.citerus.cqrs.bookstore.ordercontext.publishercontract.command.RegisterPublisherContractCommand;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@Path("publishercontract-requests")
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
  public void registerPublisher(@Valid RegisterPublisherContractRequest request) {
    PublisherContractId publisherContractId = new PublisherContractId(request.publisherContractId);
    logger.info("Registering publisher: " + publisherContractId);
    RegisterPublisherContractCommand command = commandFactory.toCommand(publisherContractId, request);
    commandBus.dispatch(command);
  }

}
