package se.citerus.cqrs.bookstore.application.web.transport;

import org.junit.Test;
import se.citerus.cqrs.bookstore.infrastructure.JsonSerializer;

import java.io.IOException;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class RegisterPublisherRequestTest {

  @Test
  public void testSerialize() throws IOException {
    RegisterPublisherRequest request1 = new RegisterPublisherRequest("ID", "Name", 5.5, 1000);
    String json = JsonSerializer.serialize(request1);
    String payload = "{\"publisherContractId\":\"ID\",\"publisherName\":\"Name\",\"fee\":5.5,\"limit\":1000}";
    assertThat(json, is(payload));

    RegisterPublisherRequest request2 = JsonSerializer.deserialize(payload, RegisterPublisherRequest.class);
    assertThat(request2, is(request1));
  }

}
