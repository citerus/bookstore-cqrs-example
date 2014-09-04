package se.citerus.cqrs.bookstore.ordercontext.query;

import org.joda.time.LocalDate;
import se.citerus.cqrs.bookstore.ordercontext.client.productcatalog.ProductCatalogClient;
import se.citerus.cqrs.bookstore.ordercontext.client.productcatalog.ProductDto;
import se.citerus.cqrs.bookstore.ordercontext.order.OrderId;
import se.citerus.cqrs.bookstore.ordercontext.order.ProductId;
import se.citerus.cqrs.bookstore.ordercontext.publishercontract.PublisherContractId;
import se.citerus.cqrs.bookstore.ordercontext.query.orderlist.OrderListDenormalizer;
import se.citerus.cqrs.bookstore.ordercontext.query.orderlist.OrderProjection;
import se.citerus.cqrs.bookstore.ordercontext.query.sales.OrdersPerDayAggregator;

import java.util.List;
import java.util.Map;

public class QueryService {

  private final OrderListDenormalizer orderListDenormalizer;
  private final OrdersPerDayAggregator ordersPerDayAggregator;
  private final ProductCatalogClient productCatalogClient;

  public QueryService(OrderListDenormalizer orderListDenormalizer,
                      OrdersPerDayAggregator ordersPerDayAggregator,
                      ProductCatalogClient productCatalogClient) {
    this.orderListDenormalizer = orderListDenormalizer;
    this.ordersPerDayAggregator = ordersPerDayAggregator;
    this.productCatalogClient = productCatalogClient;
  }

  public OrderProjection getOrder(OrderId orderId) {
    return orderListDenormalizer.get(orderId);
  }

  public List<OrderProjection> getOrders() {
    return orderListDenormalizer.getOrders();
  }

  public PublisherContractId findPublisher(ProductId productId) {
    ProductDto product = productCatalogClient.getProduct(productId.id);
    return new PublisherContractId(product.publisherContractId);
  }

  public Map<LocalDate, Integer> getOrdersPerDay() {
    return ordersPerDayAggregator.getOrdersPerDay();
  }

}
