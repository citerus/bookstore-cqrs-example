package se.citerus.cqrs.bookstore.application;

import com.google.common.io.CharStreams;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import org.junit.Ignore;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import static com.google.common.base.Charsets.UTF_8;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON_TYPE;

@Ignore
public class TestHttpClient {

  private final String url;
  private Client client;

  public TestHttpClient(String url) {
    this.url = url;
  }

  public TestHttpClient init() {
    this.client = createHttpClient();
    return this;
  }

  public int post(String contents) throws IOException {
    return client
        .resource(url)
        .entity(contents, APPLICATION_JSON_TYPE)
        .post(ClientResponse.class)
        .getStatus();
  }

  public String get() throws IOException {
    InputStream entityInputStream = client
        .resource(url)
        .accept(APPLICATION_JSON_TYPE)
        .get(ClientResponse.class)
        .getEntityInputStream();
    return CharStreams.toString(new InputStreamReader(entityInputStream, UTF_8));
  }

  private Client createHttpClient() {
    return new Client();
  }

}
