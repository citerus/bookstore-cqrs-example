package se.citerus.cqrs.bookstore.bookcatalog;


import com.sun.jersey.api.client.GenericType;
import io.dropwizard.testing.junit.ResourceTestRule;
import org.junit.After;
import org.junit.ClassRule;
import org.junit.Test;

import java.util.Collection;
import java.util.UUID;

import static java.util.Arrays.asList;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON_TYPE;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

public class BookResourceTest {

  private static final String BOOK_RESOURCE = "/books";
  private static final GenericType<Collection<BookDto>> BOOK_COLLECTION_TYPE = new GenericType<Collection<BookDto>>() {
  };

  private static BookRepository bookRepository = mock(BookRepository.class);

  @ClassRule
  public static final ResourceTestRule resources = ResourceTestRule.builder()
      .addResource(new BookResource(bookRepository))
      .build();

  @After
  public void tearDown() throws Exception {
    reset(bookRepository);
  }

  @Test
  public void listBookRequest() {
    Book book = new Book(UUID.randomUUID().toString(), "1234567890", "Book Title", "", 1000L, null);

    when(bookRepository.listBooks()).thenReturn(asList(book));

    Collection<BookDto> books = resources.client()
        .resource(BOOK_RESOURCE)
        .accept(APPLICATION_JSON_TYPE)
        .get(BOOK_COLLECTION_TYPE);

    assertThat(books, hasItem(book.toDto()));
  }

  @Test
  public void getBookRequest() {

    String bookId = UUID.randomUUID().toString();
    Book book = new Book(bookId, "1234567890", "Book Title", "", 1000L, null);

    when(bookRepository.getBook(bookId)).thenReturn(book);

    BookDto retrievedBook = resources.client()
        .resource(BOOK_RESOURCE + "/" + bookId)
        .accept(APPLICATION_JSON_TYPE)
        .get(BookDto.class);

    assertThat(retrievedBook, is(book.toDto()));
  }

}