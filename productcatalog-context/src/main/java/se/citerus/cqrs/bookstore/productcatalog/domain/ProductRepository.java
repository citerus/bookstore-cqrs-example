package se.citerus.cqrs.bookstore.productcatalog.domain;

import java.util.Collection;

public interface ProductRepository {

  Collection<Product> getProducts();

  Product getProduct(String productId);

  void save(Product product);

}
