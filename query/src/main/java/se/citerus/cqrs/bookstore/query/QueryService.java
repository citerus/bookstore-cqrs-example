package se.citerus.cqrs.bookstore.query;

import org.joda.time.LocalDate;
import se.citerus.cqrs.bookstore.book.BookId;
import se.citerus.cqrs.bookstore.order.OrderId;
import se.citerus.cqrs.bookstore.publisher.PublisherId;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class QueryService {

  private final OrderListDenormalizer orderListDenormalizer;
  private final BookCatalogDenormalizer bookCatalogDenormalizer;
  private final PublisherDenormalizer publisherDenormalizer;
  private final OrdersPerDayAggregator ordersPerDayAggregator;

  public QueryService(OrderListDenormalizer orderListDenormalizer,
                      BookCatalogDenormalizer bookCatalogDenormalizer,
                      PublisherDenormalizer publisherDenormalizer,
                      OrdersPerDayAggregator ordersPerDayAggregator) {
    this.orderListDenormalizer = orderListDenormalizer;
    this.bookCatalogDenormalizer = bookCatalogDenormalizer;
    this.publisherDenormalizer = publisherDenormalizer;
    this.ordersPerDayAggregator = ordersPerDayAggregator;
  }

  public BookProjection getBook(BookId bookId) {
    return bookCatalogDenormalizer.get(bookId);
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

  public PublisherProjection getPublisher(PublisherId publisherId) {
    return publisherDenormalizer.get(publisherId);
  }

  public PublisherId findPublisher(BookId bookId) {
    BookProjection bookProjection = bookCatalogDenormalizer.get(bookId);
    return bookProjection.hasPublisher() ? new PublisherId(bookProjection.getPublisherId()) : null;
  }

  public Map<LocalDate, Integer> getOrdersPerDay() {
    return ordersPerDayAggregator.getOrdersPerDay();
  }

}
