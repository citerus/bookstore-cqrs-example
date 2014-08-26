package se.citerus.cqrs.bookstore.bookcatalog;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class BookRepository {
  private Map<String, Book> books = new HashMap<>();

  public Collection<Book> listBooks() {
    return books.values();
  }

  public Book getBook(String bookId) {
    return books.get(bookId);
  }

  public void save(Book book) {
    this.books.put(book.bookId(), book);
  }
}
