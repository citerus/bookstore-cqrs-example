package se.citerus.cqrs.bookstore.bookcatalog.domain;

import java.util.Collection;

public interface BookRepository {

  Collection<Book> listBooks();

  Book getBook(String bookId);

  void save(Book book);

}
