package se.citerus.cqrs.bookstore.shopping.web.infrastructure;


import com.sun.jersey.api.client.Client;
import se.citerus.cqrs.bookstore.shopping.web.transport.BookDto;

public class BookClient {

  private final Client client;

  private BookClient(Client client) {
    this.client = client;
  }

  public static BookClient create(Client client) {
    return new BookClient(client);
  }

  public BookDto getBook(String bookId) {
    return client.resource("http://localhost:8080/service/books/" + bookId).get(BookDto.class);
  }
}