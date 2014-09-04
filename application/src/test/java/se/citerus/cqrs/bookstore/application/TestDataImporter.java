package se.citerus.cqrs.bookstore.application;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.io.Resources;
import org.junit.Ignore;
import se.citerus.cqrs.bookstore.ordercontext.api.RegisterPublisherContractRequest;
import se.citerus.cqrs.bookstore.productcatalog.api.ProductDto;

import java.util.List;

import static com.google.common.base.Charsets.UTF_8;
import static com.google.common.io.Resources.getResource;
import static se.citerus.cqrs.bookstore.infrastructure.JsonSerializer.deserialize;
import static se.citerus.cqrs.bookstore.infrastructure.JsonSerializer.serialize;

@Ignore
public class TestDataImporter {

  private static final String SERVER_ADDRESS = "http://localhost:8080/service";

  public static void main(String[] args) {
    importContracts("se/citerus/cqrs/bookstore/testdata/publishercontracts.json");
    importProducts("se/citerus/cqrs/bookstore/testdata/products.json");
  }

  private static void importContracts(String path) {
    try {
      TestHttpClient publisherClient = new TestHttpClient(SERVER_ADDRESS + "/publishercontract-requests").init();

      String contractsJson = Resources.toString(getResource(path), UTF_8);
      TypeReference<List<RegisterPublisherContractRequest>> listOfRegisterPublisherRequests =
          new TypeReference<List<RegisterPublisherContractRequest>>() {
          };

      List<RegisterPublisherContractRequest> requests = deserialize(contractsJson, listOfRegisterPublisherRequests);

      for (RegisterPublisherContractRequest request : requests) {
        publisherClient.post(serialize(request));
      }
      System.out.println("Imported [" + requests.size() + "] contracts...");

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private static void importProducts(String path) {
    try {
      TestHttpClient productClient = new TestHttpClient(SERVER_ADDRESS + "/products").init();

      // Add products
      String productJson = Resources.toString(getResource(path), UTF_8);
      TypeReference<List<ProductDto>> listOfProductRequests = new TypeReference<List<ProductDto>>() {
      };

      List<ProductDto> products = deserialize(productJson, listOfProductRequests);

      for (ProductDto product : products) {
        productClient.post(serialize(product));
      }
      System.out.println("Imported [" + products.size() + "] products...");

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}
