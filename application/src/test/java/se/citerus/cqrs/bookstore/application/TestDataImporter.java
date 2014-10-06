package se.citerus.cqrs.bookstore.application;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.Resources;
import org.junit.Ignore;
import se.citerus.cqrs.bookstore.ordercontext.api.RegisterPublisherContractRequest;

import java.util.List;

import static com.google.common.base.Charsets.UTF_8;
import static com.google.common.io.Resources.getResource;

@Ignore
public class TestDataImporter {

  private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
  private static final String SERVER_RESOURCE = "http://localhost:8070/publishercontract-requests";
  public static final String DEFAULT_PATH = "se/citerus/cqrs/bookstore/testdata/publishercontracts.json";

  public static void main(String[] args) {
    try {
      TestHttpClient publisherClient = new TestHttpClient(SERVER_RESOURCE).init();

      String contractsJson = Resources.toString(getResource(DEFAULT_PATH), UTF_8);
      TypeReference<List<RegisterPublisherContractRequest>> listOfRegisterPublisherRequests =
          new TypeReference<List<RegisterPublisherContractRequest>>() {
          };

      List<RegisterPublisherContractRequest> requests = OBJECT_MAPPER.readValue(contractsJson,
          listOfRegisterPublisherRequests);

      for (RegisterPublisherContractRequest request : requests) {
        publisherClient.post(OBJECT_MAPPER.writeValueAsString(request));
      }
      System.out.println("Imported [" + requests.size() + "] contracts...");

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}
