package se.citerus.cqrs.bookstore.ordercontext.order.domain;

import org.junit.Test;
import se.citerus.cqrs.bookstore.event.DomainEvent;
import se.citerus.cqrs.bookstore.ordercontext.order.BookId;
import se.citerus.cqrs.bookstore.ordercontext.order.OrderId;
import se.citerus.cqrs.bookstore.ordercontext.order.ProductId;
import se.citerus.cqrs.bookstore.ordercontext.order.event.OrderActivatedEvent;
import se.citerus.cqrs.bookstore.ordercontext.order.event.OrderPlacedEvent;

import java.util.Collections;
import java.util.List;

import static com.google.common.collect.Iterables.getLast;
import static com.google.common.collect.Iterables.getOnlyElement;
import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static se.citerus.cqrs.bookstore.ordercontext.order.OrderId.randomId;

public class OrderTest {

  private static final CustomerInformation JOHN_DOE = new CustomerInformation("John Doe", "john@acme.com", "Highway street 1");

  @Test
  public void placingAnOrder() {
    Order order = new Order();
    OrderLine orderLine = new OrderLine(ProductId.<BookId>randomId(), "title", 10, 200L);
    order.place(OrderId.<OrderId>randomId(), JOHN_DOE, asList(orderLine), 2000L);
    List<DomainEvent> uncommittedEvents = order.getUncommittedEvents();
    assertThat(uncommittedEvents.size(), is(1));
    assertThat(order.version(), is(1));
    assertThat(getOnlyElement(uncommittedEvents), instanceOf(OrderPlacedEvent.class));
  }

  @Test
  public void activatingAnOrder() {
    OrderLine orderLine = new OrderLine(ProductId.<BookId>randomId(), "title", 10, 200L);
    Order order = new Order();
    order.place(OrderId.<OrderId>randomId(), JOHN_DOE, asList(orderLine), 2000L);

    int nEvents = order.getUncommittedEvents().size();
    order.activate();

    List<DomainEvent> uncommittedEvents = order.getUncommittedEvents();
    assertThat(uncommittedEvents.size(), is(nEvents +1));
    assertThat(order.version(), is(2));
    assertThat(getLast(uncommittedEvents), instanceOf(OrderActivatedEvent.class));
  }

  @Test(expected = IllegalArgumentException.class)
  public void requireOrderLinesWhenPlacingAnOrder() {
    Order order = new Order();
    order.place(OrderId.<OrderId>randomId(), JOHN_DOE, Collections.<OrderLine>emptyList(), 2000L);
  }

  @Test(expected = IllegalStateException.class)
  public void cannotPlaceAnOrderTwice() {
    OrderLine orderLine = new OrderLine(ProductId.<BookId>randomId(), "title", 10, 200L);
    Order order = new Order();
    order.place(randomId(), JOHN_DOE, asList(orderLine), 2000L);
    order.place(randomId(), JOHN_DOE, asList(orderLine), 2000L);
  }

  @Test
  public void activatingTwiceDoesNotGenerateASecondActivationEvent() {
    Order order = new Order();
    ProductId productId = ProductId.randomId();
    long unitPrice = 200L;
    OrderLine orderLine = new OrderLine(productId, "title", 10, unitPrice);
    order.place(OrderId.<OrderId>randomId(), JOHN_DOE, asList(orderLine), 2000L);

    assertThat(order.version(), is(1));
    order.activate();
    int nEvents = order.getUncommittedEvents().size();
    order.activate();
    assertThat(order.getUncommittedEvents().size(), is(nEvents));
  }

}