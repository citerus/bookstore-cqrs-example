package se.citerus.cqrs.bookstore.application.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.citerus.cqrs.bookstore.book.BookId;
import se.citerus.cqrs.bookstore.query.BookProjection;
import se.citerus.cqrs.bookstore.query.QueryService;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.Collection;

@Path("books")
@Produces(MediaType.APPLICATION_JSON)
public class BookResource {

  private final QueryService queryService;
  private final Logger logger = LoggerFactory.getLogger(getClass());

  public BookResource(QueryService queryService) {
    this.queryService = queryService;
  }

  @GET
  public Collection<BookProjection> getBooks() {
    Collection<BookProjection> books = queryService.listBooks();
    logger.info("Returning [{}] books", books.size());
    return books;
  }

  @GET
  @Path("{bookId}")
  public BookProjection getBook(@PathParam("bookId") BookId bookId) {
    BookProjection book = queryService.findBookById(bookId);
    logger.info("Returning book: " + book);
    return book;
  }

}


