package se.citerus.cqrs.bookstore.bookcatalog.api;

import se.citerus.cqrs.bookstore.bookcatalog.domain.Book;

public class BookDtoFactory {

  public static BookDto fromBook(Book book) {
    BookDto bookDto = new BookDto();
    bookDto.bookId = book.bookId;
    bookDto.isbn = book.isbn;
    bookDto.title = book.title;
    bookDto.description = book.description;
    bookDto.price = book.price;
    bookDto.publisherContractId = book.publisherContractId;
    return bookDto;
  }

}
