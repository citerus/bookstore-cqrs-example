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
  private final BookCatalogDenormalizer bookCatalogDenormalizer;
  private final OrdersPerDayAggregator ordersPerDayAggregator;

  public QueryService(OrderListDenormalizer orderListDenormalizer,
                      BookCatalogDenormalizer bookCatalogDenormalizer,
                      OrdersPerDayAggregator ordersPerDayAggregator) {
    this.orderListDenormalizer = orderListDenormalizer;
    this.bookCatalogDenormalizer = bookCatalogDenormalizer;
    this.ordersPerDayAggregator = ordersPerDayAggregator;
  }

  public BookProjection getBook(String bookId) {
    return bookCatalogDenormalizer.get(new BookId(bookId));
  }

  public Collection<BookProjection> listBooks() {
    return bookCatalogDenormalizer.listBooks();
  }

  public OrderProjection getOrder(OrderId orderId) {
    return orderListDenormalizer.get(orderId);
  }

  public List<OrderProjection> listOrders() {
    return orderListDenormalizer.listOrders();
  }

  public PublisherContractId findPublisher(BookId bookId) {
    BookProjection bookProjection = bookCatalogDenormalizer.get(bookId);
    return bookProjection.hasPublisher() ? new PublisherContractId(bookProjection.getPublisherContractId()) : null;
  }

  public Map<LocalDate, Integer> getOrdersPerDay() {
    return ordersPerDayAggregator.getOrdersPerDay();
  }

}
