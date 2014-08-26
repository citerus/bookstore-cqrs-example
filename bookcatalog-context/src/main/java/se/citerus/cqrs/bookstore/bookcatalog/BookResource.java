package se.citerus.cqrs.bookstore.bookcatalog;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
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
  @Path("{bookId}")
  public BookDto getBook(@PathParam("bookId") String bookId) {
    Book book = bookRepository.getBook(bookId);
    logger.info("Returning book with id {}", bookId);
    return book.toDto();
  }

  @POST
  public void createBook(@Valid CreateBookRequest request) {
    Book book = new Book(request.bookId, request.isbn, request.title, request.description, request.price, request.publisherContractId);
    logger.info("Saving book with id {}", request.bookId);
    bookRepository.save(book);
  }

  @GET
  public Collection<BookDto> getBooks() {
    Collection<BookDto> books = new ArrayList<>();
    for (Book book : bookRepository.listBooks()) {
      books.add(book.toDto());
    }
    logger.info("Returning [{}] books", bookRepository.listBooks().size());
    return books;
  }

}


