package se.citerus.cqrs.bookstore.application.web;

import com.sun.jersey.api.client.GenericType;
import com.yammer.dropwizard.testing.ResourceTest;
import org.junit.Test;
import se.citerus.cqrs.bookstore.book.BookId;
import se.citerus.cqrs.bookstore.query.BookProjection;
import se.citerus.cqrs.bookstore.query.QueryService;

import java.util.Collection;

import static java.util.Arrays.asList;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON_TYPE;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BookResourceTest extends ResourceTest {

  private static final String BOOK_RESOURCE = "http://localhost:8080/books";
  private static final GenericType<Collection<BookProjection>> BOOK_COLLECTION_TYPE = new GenericType<Collection<BookProjection>>() {
  };
  private QueryService queryService = mock(QueryService.class);

  @Override
  protected void setUpResources() {
    addResource(new BookResource(queryService));
  }

  @Test
  public void listBookRequest() {
    BookProjection book = new BookProjection(BookId.<BookId>randomId().id, "1234567890", "Book Title", "", 1000L, null);

    when(queryService.listBooks()).thenReturn(asList(book));

    Collection<BookProjection> books = client()
        .resource(BOOK_RESOURCE)
        .accept(APPLICATION_JSON_TYPE)
        .get(BOOK_COLLECTION_TYPE);

    assertThat(books, hasItem(book));
  }

  @Test
  public void getBookRequest() {
    BookId bookId = BookId.randomId();
    BookProjection book = new BookProjection(bookId.id, "1234567890", "Book Title", "", 1000L, null);

    when(queryService.findBookById(bookId)).thenReturn(book);

    BookProjection retrievedBook = client()
        .resource(BOOK_RESOURCE + "/" + bookId.id)
        .accept(APPLICATION_JSON_TYPE)
        .get(BookProjection.class);

    assertThat(retrievedBook, is(book));
  }
}
