package se.citerus.cqrs.bookstore.bookcatalog.infrastructure;

import se.citerus.cqrs.bookstore.bookcatalog.domain.Book;
import se.citerus.cqrs.bookstore.bookcatalog.domain.BookRepository;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class InMemoryBookRepository implements BookRepository {

  private Map<String, Book> books = new HashMap<>();

  @Override
  public Collection<Book> listBooks() {
    return books.values();
  }

  @Override
  public Book getBook(String bookId) {
    return books.get(bookId);
  }

  @Override
  public void save(Book book) {
    this.books.put(book.bookId, book);
  }

}
