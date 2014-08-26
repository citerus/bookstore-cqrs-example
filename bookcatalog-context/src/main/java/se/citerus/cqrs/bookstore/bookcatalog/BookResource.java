package se.citerus.cqrs.bookstore.bookcatalog;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.Collection;

@Path("books")
@Produces(MediaType.APPLICATION_JSON)
public class BookResource {

  private final Logger logger = LoggerFactory.getLogger(getClass());
  private BookRepository bookRepository;


  public BookResource(BookRepository bookRepository) {
    this.bookRepository = bookRepository;
  }

  @GET
  public Collection<Book> getBooks() {
    Collection<Book> books = bookRepository.listBooks();
    logger.info("Returning [{}] books", books.size());
    return books;
  }

}


