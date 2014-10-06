package se.citerus.cqrs.bookstore.productcatalog.application;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.Resources;
import org.junit.Ignore;
import se.citerus.cqrs.bookstore.productcatalog.api.ProductDto;

import java.util.List;

import static com.google.common.base.Charsets.UTF_8;
import static com.google.common.io.Resources.getResource;

@Ignore
public class TestProductDataImporter {

  private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
  private static final String SERVER_RESOURCE = "http://localhost:8090/products";
  private static final String DEFAULT_PATH = "test-products.json";

  public static void main(String[] args) {
    try {
      TestHttpClient productClient = new TestHttpClient(SERVER_RESOURCE).init();

      // Add products
      String productJson = Resources.toString(getResource(DEFAULT_PATH), UTF_8);
      TypeReference<List<ProductDto>> listOfProductRequests = new TypeReference<List<ProductDto>>() {
      };

      List<ProductDto> products = OBJECT_MAPPER.readValue(productJson, listOfProductRequests);

      for (ProductDto product : products) {
        productClient.post(OBJECT_MAPPER.writeValueAsString(product));
      }
      System.out.println("Imported [" + products.size() + "] products...");

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}
