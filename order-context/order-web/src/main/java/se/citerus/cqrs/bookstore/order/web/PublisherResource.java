package se.citerus.cqrs.bookstore.order.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.citerus.cqrs.bookstore.command.CommandBus;
import se.citerus.cqrs.bookstore.order.publisher.command.RegisterPublisherContractCommand;
import se.citerus.cqrs.bookstore.publisher.PublisherContractId;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("publisher-requests")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)

public class PublisherResource {


  private final Logger logger = LoggerFactory.getLogger(getClass());
  private final CommandBus commandBus;
  private final CommandFactory commandFactory = new CommandFactory();

  public PublisherResource(CommandBus commandBus) {
    this.commandBus = commandBus;
  }

  @POST
  @Path("register")
  public void registerPublisher(@Valid RegisterPublisherRequest registerPublisherRequest) {
    PublisherContractId publisherContractId = new PublisherContractId(registerPublisherRequest.publisherContractId);
    logger.info("Registering publisher: " + publisherContractId);
    RegisterPublisherContractCommand command = commandFactory.toCommand(publisherContractId, registerPublisherRequest);
    commandBus.dispatch(command);
  }

}
