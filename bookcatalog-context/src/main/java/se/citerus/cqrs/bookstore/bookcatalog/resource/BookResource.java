package se.citerus.cqrs.bookstore.bookcatalog.resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.citerus.cqrs.bookstore.bookcatalog.api.BookDto;
import se.citerus.cqrs.bookstore.bookcatalog.api.BookDtoFactory;
import se.citerus.cqrs.bookstore.bookcatalog.domain.Book;
import se.citerus.cqrs.bookstore.bookcatalog.domain.BookRepository;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.Collection;

@Path("books")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BookResource {

  private final Logger logger = LoggerFactory.getLogger(getClass());
  private BookRepository bookRepository;

  public BookResource(BookRepository bookRepository) {
    this.bookRepository = bookRepository;
  }

  @GET
  @Path("{bookId}")
  public BookDto getBook(@PathParam("bookId") String bookId) {
    Book book = bookRepository.getBook(bookId);
    if (book == null) {
      throw new IllegalArgumentException("No such book: " + bookId);
    }
    logger.info("Returning book with id {}", book.bookId);
    return BookDtoFactory.fromBook(book);
  }

  @POST
  public void createBook(@Valid BookDto request) {
    Book book = new Book(request.bookId, request.isbn, request.title, request.description, request.price, request.publisherContractId);
    logger.info("Saving book with id {}", request.bookId);
    bookRepository.save(book);
  }

  @GET
  public Collection<BookDto> getBooks() {
    Collection<BookDto> books = new ArrayList<>();
    for (Book book : bookRepository.listBooks()) {
      books.add(BookDtoFactory.fromBook(book));
    }
    logger.info("Returning [{}] books", bookRepository.listBooks().size());
    return books;
  }

}


