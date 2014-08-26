package se.citerus.cqrs.bookstore.application.web;

import com.sun.jersey.api.client.GenericType;
import io.dropwizard.testing.junit.ResourceTestRule;
import org.junit.After;
import org.junit.ClassRule;
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
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

public class BookResourceTest {

  private static final String BOOK_RESOURCE = "http://localhost:8080/books";
  private static final GenericType<Collection<BookProjection>> BOOK_COLLECTION_TYPE = new GenericType<Collection<BookProjection>>() {
  };

  private static final QueryService queryService = mock(QueryService.class);


  @ClassRule
  public static final ResourceTestRule resources = ResourceTestRule.builder()
      .addResource(new BookResource(queryService))
      .build();

  @After
  public void tearDown() throws Exception {
    reset(queryService);
  }

  @Test
  public void listBookRequest() {
    BookProjection book = new BookProjection(BookId.<BookId>randomId().id, "1234567890", "Book Title", "", 1000L, null);

    when(queryService.listBooks()).thenReturn(asList(book));

    Collection<BookProjection> books = resources.client()
        .resource(BOOK_RESOURCE)
        .accept(APPLICATION_JSON_TYPE)
        .get(BOOK_COLLECTION_TYPE);

    assertThat(books, hasItem(book));
  }

  @Test
  public void getBookRequest() {
    BookId bookId = BookId.randomId();
    BookProjection book = new BookProjection(bookId.id, "1234567890", "Book Title", "", 1000L, null);

    when(queryService.getBook(bookId.id)).thenReturn(book);

    BookProjection retrievedBook = resources.client()
        .resource(BOOK_RESOURCE + "/" + bookId.id)
        .accept(APPLICATION_JSON_TYPE)
        .get(BookProjection.class);

    assertThat(retrievedBook, is(book));
  }
}
