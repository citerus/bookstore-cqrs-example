package se.citerus.cqrs.bookstore.productcatalog.resource;

import io.dropwizard.testing.junit.ResourceTestRule;
import org.junit.After;
import org.junit.ClassRule;
import org.junit.Test;
import se.citerus.cqrs.bookstore.productcatalog.api.ProductDto;
import se.citerus.cqrs.bookstore.productcatalog.api.ProductDtoFactory;
import se.citerus.cqrs.bookstore.productcatalog.domain.Book;
import se.citerus.cqrs.bookstore.productcatalog.domain.Product;
import se.citerus.cqrs.bookstore.productcatalog.domain.ProductRepository;

import javax.ws.rs.core.GenericType;
import java.util.Collection;
import java.util.UUID;

import static java.util.Arrays.asList;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON_TYPE;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

public class ProductResourceTest {

  private static final String PRODUCT_RESOURCE = "/products";
  private static final GenericType<Collection<ProductDto>> PRODUCTS_COLLECTION_TYPE =
      new GenericType<Collection<ProductDto>>() {
      };

  private static ProductRepository productRepository = mock(ProductRepository.class);

  @ClassRule
  public static final ResourceTestRule resources = ResourceTestRule.builder()
      .addResource(new ProductResource(productRepository))
      .build();

  @After
  public void tearDown() throws Exception {
    reset(productRepository);
  }

  @Test
  public void getProductsRequest() {
    Book book = new Book(UUID.randomUUID().toString(), "1234567890", "Book Title", "");
    Product product = new Product(UUID.randomUUID().toString(), book, 1000L, null);
    when(productRepository.getProducts()).thenReturn(asList(product));

    Collection<ProductDto> products = resources.client()
        .target(PRODUCT_RESOURCE)
        .request(APPLICATION_JSON_TYPE)
        .get(PRODUCTS_COLLECTION_TYPE);

    assertThat(products, hasItem(ProductDtoFactory.fromProduct(product)));
  }

  @Test
  public void getProductRequest() {

    String bookId = UUID.randomUUID().toString();
    Book book = new Book(bookId, "1234567890", "Book Title", "");
    String productId = UUID.randomUUID().toString();
    Product product = new Product(productId, book, 1000L, null);
    when(productRepository.getProduct(productId)).thenReturn(product);

    ProductDto retrievedProduct = resources.client()
        .target(PRODUCT_RESOURCE + "/" + productId)
        .request(APPLICATION_JSON_TYPE)
        .get(ProductDto.class);

    assertThat(retrievedProduct, is(ProductDtoFactory.fromProduct(product)));
  }

}