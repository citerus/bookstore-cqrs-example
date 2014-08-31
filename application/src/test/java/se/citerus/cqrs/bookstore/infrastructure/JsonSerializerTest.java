package se.citerus.cqrs.bookstore.infrastructure;

import org.hibernate.validator.constraints.NotEmpty;
import org.junit.Test;
import se.citerus.cqrs.bookstore.TransportObject;
import se.citerus.cqrs.bookstore.ordercontext.api.RegisterPublisherContractRequest;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.IOException;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static se.citerus.cqrs.bookstore.GenericId.ID_PATTERN;

public class JsonSerializerTest {

  @Test
  public void testSerialize() throws IOException {
    String json = "    {\n" +
        "        \"someId\":\"11113865-24e7-4c7c-8b93-eb6caac48111\",\n" +
        "        \"name\":\"John\",\n" +
        "        \"doubleValue\":5.5,\n" +
        "        \"longValue\":1000\n" +
        "    }\n";

    JsonClass request = JsonSerializer.deserialize(json, JsonClass.class);
    assertThat(request.toString(), is("JsonSerializerTest.JsonClass[someId=11113865-24e7-4c7c-8b93-eb6caac48111,name=John,doubleValue=5.5,longValue=1000]"));
  }

  private static class JsonClass extends TransportObject {

    @NotEmpty
    @Pattern(regexp = ID_PATTERN)
    public String someId;

    @NotNull
    public String name;

    @Min(1)
    public double doubleValue;

    @Min(1)
    public long longValue;

  }

}
