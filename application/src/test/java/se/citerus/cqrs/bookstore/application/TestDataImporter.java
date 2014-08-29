package se.citerus.cqrs.bookstore.application;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.io.Resources;
import org.junit.Ignore;
import se.citerus.cqrs.bookstore.bookcatalog.api.BookDto;
import se.citerus.cqrs.bookstore.infrastructure.JsonSerializer;
import se.citerus.cqrs.bookstore.ordercontext.api.RegisterPublisherContractRequest;

import java.util.List;

import static com.google.common.base.Charsets.UTF_8;
import static com.google.common.io.Resources.getResource;
import static se.citerus.cqrs.bookstore.infrastructure.JsonSerializer.deserialize;

@Ignore
public class TestDataImporter {

  private static final String SERVER_ADDRESS = "http://localhost:8080/service";

  public static void main(String[] args) {
    importBooks();
  }

  private static void importBooks() {

    try {
      TestHttpClient publisherClient = new TestHttpClient(SERVER_ADDRESS + "/publishercontract-requests").init();

      // Add publisher contracts
      String contractsJson = Resources.toString(getResource("se/citerus/cqrs/bookstore/testdata/publishercontracts.json"), UTF_8);
      TypeReference<List<RegisterPublisherContractRequest>> listOfRegisterPublisherRequests = new TypeReference<List<RegisterPublisherContractRequest>>() {
      };
      List<RegisterPublisherContractRequest> publishersRequests = deserialize(contractsJson, listOfRegisterPublisherRequests);

      for (RegisterPublisherContractRequest request : publishersRequests) {
        publisherClient.post(JsonSerializer.serialize(request));
      }

      TestHttpClient bookClient = new TestHttpClient(SERVER_ADDRESS + "/books").init();

      // Add books
      String booksJson = Resources.toString(getResource("se/citerus/cqrs/bookstore/testdata/books.json"), UTF_8);
      TypeReference<List<BookDto>> listOfCreateBookRequests = new TypeReference<List<BookDto>>() {
      };

      List<BookDto> books = deserialize(booksJson, listOfCreateBookRequests);

      for (BookDto book : books) {
        bookClient.post(JsonSerializer.serialize(book));
      }

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}
