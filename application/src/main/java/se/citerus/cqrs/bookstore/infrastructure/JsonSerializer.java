package se.citerus.cqrs.bookstore.infrastructure;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public final class JsonSerializer {

  private JsonSerializer() {
  }

  private static final ObjectMapper objectMapper = new ObjectMapper();

  public static String serialize(Object o) throws IOException {
    return objectMapper.writeValueAsString(o);
  }

  public static <T> T deserialize(String body, Class<T> clazz) throws IOException {
    return objectMapper.readValue(body, clazz);
  }

  public static <T> T deserialize(String body, TypeReference<T> type) throws IOException {
    return objectMapper.readValue(body, type);
  }

}
