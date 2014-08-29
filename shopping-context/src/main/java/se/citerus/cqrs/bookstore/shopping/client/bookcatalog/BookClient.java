package se.citerus.cqrs.bookstore.shopping.client.bookcatalog;


import com.sun.jersey.api.client.Client;

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