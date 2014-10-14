package se.citerus.cqrs.bookstore.productcatalog.infrastructure;

import se.citerus.cqrs.bookstore.productcatalog.domain.Product;
import se.citerus.cqrs.bookstore.productcatalog.domain.ProductRepository;

import java.util.*;

public class InMemoryProductRepository implements ProductRepository {

  private Map<String, Product> products = new HashMap<>();

  private static final Comparator<Product> PRODUCT_COMPARATOR = new Comparator<Product>() {
    @Override
    public int compare(Product o1, Product o2) {
      return o1.book.title.compareTo(o2.book.title);
    }
  };

  @Override
  public List<Product> getProducts() {
    List<Product> values = new ArrayList<>(products.values());
    Collections.sort(values, PRODUCT_COMPARATOR);
    return values;
  }

  @Override
  public Product getProduct(String productId) {
    return products.get(productId);
  }

  @Override
  public void save(Product product) {
    this.products.put(product.productId, product);
  }

}
