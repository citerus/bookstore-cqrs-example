package se.citerus.cqrs.bookstore.application;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.io.Resources;
import org.junit.Ignore;
import se.citerus.cqrs.bookstore.application.web.transport.CreateBookRequest;
import se.citerus.cqrs.bookstore.application.web.transport.RegisterPublisherRequest;
import se.citerus.cqrs.bookstore.infrastructure.JsonSerializer;

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
      TestHttpClient publisherClient = new TestHttpClient(SERVER_ADDRESS + "/admin/register-publisher-requests").init();

      // Add publishers
      String publishersJson = Resources.toString(getResource("se/citerus/cqrs/bookstore/testdata/publishers.json"), UTF_8);
      TypeReference<List<RegisterPublisherRequest>> listOfRegisterPublisherRequests = new TypeReference<List<RegisterPublisherRequest>>() {
      };
      List<RegisterPublisherRequest> publishersRequests = deserialize(publishersJson, listOfRegisterPublisherRequests);

      for (RegisterPublisherRequest request : publishersRequests) {
        publisherClient.post(JsonSerializer.serialize(request));
      }

      TestHttpClient bookClient = new TestHttpClient(SERVER_ADDRESS + "/admin/create-book-requests").init();

      // Add books
      String booksJson = Resources.toString(getResource("se/citerus/cqrs/bookstore/testdata/books.json"), UTF_8);
      TypeReference<List<CreateBookRequest>> listOfCreateBookRequests = new TypeReference<List<CreateBookRequest>>() {
      };
      List<CreateBookRequest> bookRequests = deserialize(booksJson, listOfCreateBookRequests);

      for (CreateBookRequest bookRequest : bookRequests) {
        bookClient.post(JsonSerializer.serialize(bookRequest));
      }

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}
