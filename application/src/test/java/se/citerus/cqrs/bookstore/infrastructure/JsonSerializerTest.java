package se.citerus.cqrs.bookstore.infrastructure;

import org.junit.Test;
import se.citerus.cqrs.bookstore.ordercontext.api.RegisterPublisherContractRequest;

import java.io.IOException;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class JsonSerializerTest {

  @Test
  public void testSerialize() throws IOException {
    String publisherJson = "    {\n" +
        "        \"publisherContractId\":\"11113865-24e7-4c7c-8b93-eb6caac48111\",\n" +
        "        \"publisherName\":\"Addison-Wesley\",\n" +
        "        \"feePercentage\":5.5,\n" +
        "        \"limit\":1000\n" +
        "    }\n";

    RegisterPublisherContractRequest command = JsonSerializer.deserialize(
        publisherJson, RegisterPublisherContractRequest.class);
    assertThat(command.toString(), is("RegisterPublisherContractRequest[" +
        "publisherContractId=11113865-24e7-4c7c-8b93-eb6caac48111,publisherName=Addison-Wesley,feePercentage=5.5,limit=1000" +
        "]"));
  }

  // TODO: Replace RPCR with inner static class suited for testing only.

}
