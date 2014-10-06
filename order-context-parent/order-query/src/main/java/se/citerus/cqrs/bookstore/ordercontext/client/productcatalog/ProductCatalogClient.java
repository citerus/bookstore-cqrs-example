package se.citerus.cqrs.bookstore.ordercontext.client.productcatalog;

import com.sun.jersey.api.client.Client;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON_TYPE;

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
    return client.resource(serviceUrl + productId)
        .accept(APPLICATION_JSON_TYPE).get(ProductDto.class);
  }

}
