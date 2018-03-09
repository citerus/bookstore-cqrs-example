package se.citerus.cqrs.bookstore.productcatalog.application;

import com.google.common.io.CharStreams;
import org.junit.Ignore;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
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
        .target(url)
        .request()
        .post(Entity.json(contents), Response.class)
        .getStatus();
  }

  public String get() throws IOException {
    InputStream entityInputStream = client
        .target(url)
        .request(APPLICATION_JSON_TYPE)
        .get(InputStream.class);
    return CharStreams.toString(new InputStreamReader(entityInputStream, UTF_8));
  }

  private Client createHttpClient() {
    return ClientBuilder. newClient();
  }

}
