package se.citerus.cqrs.bookstore.productcatalog.resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.citerus.cqrs.bookstore.productcatalog.api.BookDto;
import se.citerus.cqrs.bookstore.productcatalog.api.ProductDto;
import se.citerus.cqrs.bookstore.productcatalog.api.ProductDtoFactory;
import se.citerus.cqrs.bookstore.productcatalog.domain.Book;
import se.citerus.cqrs.bookstore.productcatalog.domain.Product;
import se.citerus.cqrs.bookstore.productcatalog.domain.ProductRepository;

import javax.validation.Valid;
import javax.ws.rs.*;
import java.util.ArrayList;
import java.util.Collection;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@Path("products")
@Produces(APPLICATION_JSON)
@Consumes(APPLICATION_JSON)
public class ProductResource {

  private final Logger logger = LoggerFactory.getLogger(getClass());
  private ProductRepository productRepository;

  public ProductResource(ProductRepository productRepository) {
    this.productRepository = productRepository;
  }

  @GET
  @Path("{productId}")
  public ProductDto getProduct(@PathParam("productId") String productId) {
    Product product = productRepository.getProduct(productId);
    if (product == null) {
      throw new IllegalArgumentException("No such product: " + productId);
    }
    logger.info("Returning product with id [{}]", product.productId);
    return ProductDtoFactory.fromProduct(product);
  }

  @POST
  public void createProduct(@Valid ProductDto request) {
    BookDto bookDto = request.book;
    Book book = new Book(request.book.bookId, bookDto.isbn, bookDto.title, bookDto.description);
    Product product = new Product(request.productId, book, request.price, request.publisherContractId);
    logger.info("Saving product with id [{}]", request.productId);
    productRepository.save(product);
  }

  @GET
  public Collection<ProductDto> getProducts() {
    Collection<ProductDto> products = new ArrayList<>();
    for (Product product : productRepository.getProducts()) {
      products.add(ProductDtoFactory.fromProduct(product));
    }
    logger.info("Returning [{}] products", products.size());
    return products;
  }

}


