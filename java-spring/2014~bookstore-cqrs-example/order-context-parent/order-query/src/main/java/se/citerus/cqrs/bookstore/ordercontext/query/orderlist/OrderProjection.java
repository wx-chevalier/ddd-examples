package se.citerus.cqrs.bookstore.ordercontext.query.orderlist;

import com.fasterxml.jackson.annotation.JsonProperty;
import se.citerus.cqrs.bookstore.ordercontext.order.OrderId;
import se.citerus.cqrs.bookstore.ordercontext.order.OrderStatus;
import se.citerus.cqrs.bookstore.ordercontext.query.Projection;

import java.util.ArrayList;
import java.util.List;

public class OrderProjection extends Projection {

  private final OrderId orderId;
  private final long orderPlacedTimestamp;
  private final long orderAmount;
  private final String customerName;
  private final List<OrderLineProjection> orderLines;
  private OrderStatus status;

  public OrderProjection(@JsonProperty("orderId") OrderId orderId,
                         @JsonProperty("orderPlacedTimestamp") long orderPlacedTimestamp,
                         @JsonProperty("customerName") String customerName,
                         @JsonProperty("orderAmount") long orderAmount,
                         @JsonProperty("orderLines") List<OrderLineProjection> orderLines,
                         @JsonProperty("status") OrderStatus status) {
    this.orderId = orderId;
    this.orderPlacedTimestamp = orderPlacedTimestamp;
    this.customerName = customerName;
    this.orderAmount = orderAmount;
    this.orderLines = new ArrayList<>(orderLines);
    this.status = status;
  }

  public List<OrderLineProjection> getOrderLines() {
    return orderLines;
  }

  public OrderId getOrderId() {
    return orderId;
  }

  public String getCustomerName() {
    return customerName;
  }

  public long getOrderAmount() {
    return orderAmount;
  }

  public OrderStatus getStatus() {
    return status;
  }

  public void setStatus(OrderStatus status) {
    this.status = status;
  }

  public long getOrderPlacedTimestamp() {
    return orderPlacedTimestamp;
  }

}
