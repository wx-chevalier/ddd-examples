package se.citerus.cqrs.bookstore.ordercontext.query.sales;

import org.joda.time.LocalDate;
import org.junit.Test;
import se.citerus.cqrs.bookstore.ordercontext.order.OrderId;
import se.citerus.cqrs.bookstore.ordercontext.order.OrderLine;
import se.citerus.cqrs.bookstore.ordercontext.order.event.OrderPlacedEvent;

import java.util.Collections;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class OrdersPerDayAggregatorTest {

  private final OrdersPerDayAggregator ordersPerDayAggregator = new OrdersPerDayAggregator();

  @Test
  public void testHandleEvent() {
    OrderPlacedEvent book1 = createOrder(2013, 1, 1);
    OrderPlacedEvent book2 = createOrder(2013, 1, 2);
    OrderPlacedEvent book3 = createOrder(2013, 1, 2);

    ordersPerDayAggregator.handleEvent(book1);
    ordersPerDayAggregator.handleEvent(book2);
    ordersPerDayAggregator.handleEvent(book3);

    assertThat(ordersPerDayAggregator.getOrdersPerDay().toString(), is("{2013-01-01=1, 2013-01-02=2}"));
  }

  private OrderPlacedEvent createOrder(int year, int month, int day) {
    long timestamp = new LocalDate(year, month, day).toDate().getTime();
    return new OrderPlacedEvent(OrderId.<OrderId>randomId(), 1, timestamp, null, Collections.<OrderLine>emptyList(), 0);
  }

}
