package se.citerus.cqrs.bookstore.shopping.client.productcatalog;

import com.sun.jersey.api.client.Client;

public class ProductCatalogClient {

  private final Client client;

  private ProductCatalogClient(Client client) {
    this.client = client;
  }

  public static ProductCatalogClient create(Client client) {
    return new ProductCatalogClient(client);
  }

  public ProductDto getProduct(String productId) {
    return client.resource("http://localhost:8080/service/products/" + productId).get(ProductDto.class);
  }
}