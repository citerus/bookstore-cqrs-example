package se.citerus.cqrs.bookstore.ordercontext.client.productcatalog;

import com.sun.jersey.api.client.Client;

import javax.ws.rs.core.MediaType;

public class ProductCatalogClient {

  private final Client client;

  private ProductCatalogClient(Client client) {
    this.client = client;
  }

  public static ProductCatalogClient create(Client client) {
    return new ProductCatalogClient(client);
  }

  public ProductDto getProduct(String productId) {
    return client.resource("http://localhost:8080/service/products/" + productId)
        .accept(MediaType.APPLICATION_JSON_TYPE).get(ProductDto.class);
  }

}
