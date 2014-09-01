package se.citerus.cqrs.bookstore.application;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.io.Resources;
import org.junit.Ignore;
import se.citerus.cqrs.bookstore.bookcatalog.api.BookDto;
import se.citerus.cqrs.bookstore.ordercontext.api.RegisterPublisherContractRequest;

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
    importBooks("se/citerus/cqrs/bookstore/testdata/books.json");
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

  private static void importBooks(String path) {
    try {
      TestHttpClient bookClient = new TestHttpClient(SERVER_ADDRESS + "/books").init();

      // Add books
      String booksJson = Resources.toString(getResource(path), UTF_8);
      TypeReference<List<BookDto>> listOfCreateBookRequests = new TypeReference<List<BookDto>>() {
      };

      List<BookDto> requests = deserialize(booksJson, listOfCreateBookRequests);

      for (BookDto book : requests) {
        bookClient.post(serialize(book));
      }
      System.out.println("Imported [" + requests.size() + "] books...");

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}
