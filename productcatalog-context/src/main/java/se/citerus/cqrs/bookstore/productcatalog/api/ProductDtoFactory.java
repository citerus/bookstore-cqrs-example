package se.citerus.cqrs.bookstore.productcatalog.api;

import se.citerus.cqrs.bookstore.productcatalog.domain.Book;
import se.citerus.cqrs.bookstore.productcatalog.domain.Product;

public class ProductDtoFactory {

  public static ProductDto fromProduct(Product product) {
    return toProduct(product, toBook(product.book));
  }

  private static ProductDto toProduct(Product product, BookDto bookDto) {
    ProductDto productDto = new ProductDto();
    productDto.productId = product.productId;
    productDto.price = product.price;
    productDto.publisherContractId = product.publisherContractId;
    productDto.book = bookDto;
    return productDto;
  }

  private static BookDto toBook(Book book) {
    BookDto bookDto = new BookDto();
    bookDto.bookId = book.bookId;
    bookDto.isbn = book.isbn;
    bookDto.title = book.title;
    bookDto.description = book.description;
    return bookDto;
  }

}
