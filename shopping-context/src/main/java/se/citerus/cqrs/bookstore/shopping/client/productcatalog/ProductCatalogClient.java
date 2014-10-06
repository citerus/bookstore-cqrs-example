package se.citerus.cqrs.bookstore.shopping.client.productcatalog;

import com.sun.jersey.api.client.Client;

public class ProductCatalogClient {

  private final Client client;
  private final String serviceUrl;

  private ProductCatalogClient(Client client, String serviceUrl) {
    this.client = client;
    this.serviceUrl = serviceUrl;
  }

  public static ProductCatalogClient create(Client client, String serviceUrl) {
    return new ProductCatalogClient(client, serviceUrl);
  }

  public ProductDto getProduct(String productId) {
    return client.resource(serviceUrl + productId).get(ProductDto.class);
  }

}