package se.citerus.cqrs.bookstore.ordercontext.client.productcatalog;

import com.sun.jersey.api.client.Client;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON_TYPE;

public class ProductCatalogClient {

  private final Client client;

  private ProductCatalogClient(Client client) {
    this.client = client;
  }

  public static ProductCatalogClient create(Client client) {
    return new ProductCatalogClient(client);
  }

  public ProductDto getProduct(String productId) {
    return client.resource("http://localhost:8090/products/" + productId)
        .accept(APPLICATION_JSON_TYPE).get(ProductDto.class);
  }

}
