package se.citerus.cqrs.bookstore.query;

import com.google.common.eventbus.Subscribe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.citerus.cqrs.bookstore.book.BookId;
import se.citerus.cqrs.bookstore.book.event.BookCreatedEvent;
import se.citerus.cqrs.bookstore.book.event.BookPriceUpdatedEvent;
import se.citerus.cqrs.bookstore.event.DomainEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BookCatalogDenormalizer implements DomainEventListener {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  private final BookTitleComparator bookTitleComparator = new BookTitleComparator();
  private Map<BookId, BookProjection> books = new HashMap<>();

  @Subscribe
  public void handleEvent(BookCreatedEvent event) {
    logger.info("Received: " + event.toString());
    String publisherId = event.publisherId != null ? event.publisherId.id : null;
    BookProjection book = new BookProjection(
        event.aggregateId.id, event.isbn, event.title, event.description, event.price, publisherId);
    books.put(event.aggregateId, book);
  }

  @Subscribe
  public void handleEvent(BookPriceUpdatedEvent event) {
    logger.info("Received: " + event.toString());
    BookProjection projection = books.get(event.aggregateId);
    projection.setPrice(event.newPrice);
  }

  @Override
  public boolean supportsReplay() {
    return true;
  }

  public Collection<BookProjection> listBooks() {
    List<BookProjection> projections = new ArrayList<>(books.values());
    Collections.sort(projections, bookTitleComparator);
    return projections;
  }

  public BookProjection findById(BookId bookId) {
    return books.get(bookId);
  }

  private static class BookTitleComparator implements Comparator<BookProjection> {
    @Override
    public int compare(BookProjection o1, BookProjection o2) {
      return o1.getTitle().compareTo(o2.getTitle());
    }
  }

}
