package se.citerus.cqrs.bookstore.shopping.web;


import com.sun.jersey.api.client.Client;
import se.citerus.cqrs.bookstore.shopping.web.transport.BookProjection;

public class BookClient {

  private final Client client;

  private BookClient(Client client) {
    this.client = client;
  }

  public static BookClient create(Client client) {
    return new BookClient(client);
  }

  public BookProjection getBook(String bookId) {
    return client.resource("http://localhost:8080/books/" + bookId).get(BookProjection.class);
  }
}