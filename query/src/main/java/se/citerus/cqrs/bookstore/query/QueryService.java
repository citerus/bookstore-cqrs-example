package se.citerus.cqrs.bookstore.query;

import org.joda.time.LocalDate;
import se.citerus.cqrs.bookstore.book.BookId;
import se.citerus.cqrs.bookstore.order.OrderId;
import se.citerus.cqrs.bookstore.publisher.PublisherContractId;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class QueryService {

  private final OrderListDenormalizer orderListDenormalizer;
  private final OrdersPerDayAggregator ordersPerDayAggregator;
  private final BookCatalogClient bookCatalogClient;

  public QueryService(OrderListDenormalizer orderListDenormalizer,
                      OrdersPerDayAggregator ordersPerDayAggregator,
                      BookCatalogClient bookCatalogClient) {
    this.orderListDenormalizer = orderListDenormalizer;
    this.ordersPerDayAggregator = ordersPerDayAggregator;
    this.bookCatalogClient = bookCatalogClient;
  }

  public OrderProjection getOrder(OrderId orderId) {
    return orderListDenormalizer.get(orderId);
  }

  public List<OrderProjection> listOrders() {
    return orderListDenormalizer.listOrders();
  }

  public PublisherContractId findPublisher(BookId bookId) {
    BookDto book = bookCatalogClient.getBook(bookId.id);
    return book.hasPublisher() ? new PublisherContractId(book.publisherContractId) : null;
  }

  public Map<LocalDate, Integer> getOrdersPerDay() {
    return ordersPerDayAggregator.getOrdersPerDay();
  }

}
