package se.citerus.cqrs.bookstore.ordercontext.resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.citerus.cqrs.bookstore.command.CommandBus;
import se.citerus.cqrs.bookstore.ordercontext.api.ActivateOrderRequest;
import se.citerus.cqrs.bookstore.ordercontext.api.PlaceOrderRequest;
import se.citerus.cqrs.bookstore.ordercontext.order.command.ActivateOrderCommand;
import se.citerus.cqrs.bookstore.ordercontext.order.command.CommandFactory;
import se.citerus.cqrs.bookstore.ordercontext.order.command.PlaceOrderCommand;

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
  private final CommandBus commandBus;
  private final CommandFactory commandFactory = new CommandFactory();

  public OrderResource(CommandBus commandBus) {
    this.commandBus = commandBus;
  }

  @POST
  public void placeOrder(@Valid PlaceOrderRequest placeOrderRequest) {
    logger.info("Placing customer order: " + placeOrderRequest);
    PlaceOrderCommand placeOrderCommand = commandFactory.toCommand(placeOrderRequest);
    commandBus.dispatch(placeOrderCommand);
  }

  @POST
  @Path("activations")
  public void activateOrder(@Valid ActivateOrderRequest activationRequest) {
    logger.info("Activating orderId: " + activationRequest.orderId);
    ActivateOrderCommand command = commandFactory.toCommand(activationRequest);
    commandBus.dispatch(command);
  }

}
