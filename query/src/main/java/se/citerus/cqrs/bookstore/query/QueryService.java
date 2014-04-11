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

  public Collection<BookProjection> listBooks() {
    return bookCatalogDenormalizer.listBooks();
  }

  public BookProjection findBookById(BookId bookId) {
    return bookCatalogDenormalizer.findById(bookId);
  }

  public PublisherId findPublisher(BookId bookId) {
    BookProjection bookProjection = bookCatalogDenormalizer.findById(bookId);
    return bookProjection.hasPublisher() ? new PublisherId(bookProjection.getPublisherId()) : null;
  }

  public List<OrderProjection> listOrders() {
    return orderListDenormalizer.listOrders();
  }

  public OrderProjection getById(OrderId orderId) {
    return orderListDenormalizer.findById(orderId);
  }

  public PublisherProjection findPublisherById(PublisherId publisherId) {
    return publisherDenormalizer.findById(publisherId);
  }

  public Map<LocalDate, Integer> getOrdersPerDay() {
    return ordersPerDayAggregator.getOrdersPerDay();
  }

}
