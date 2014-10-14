package se.citerus.cqrs.bookstore.ordercontext.application.infrastructure;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.Files;
import se.citerus.cqrs.bookstore.GenericId;
import se.citerus.cqrs.bookstore.domain.AggregateRoot;
import se.citerus.cqrs.bookstore.event.DomainEvent;
import se.citerus.cqrs.bookstore.event.DomainEventStore;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Charsets.UTF_8;

public class SimpleFileBasedEventStore implements DomainEventStore {

  private static final String DEFAULT_FILE_NAME = "events.txt";
  private final File eventStoreFile;
  private final ObjectMapper objectMapper = new ObjectMapper();

  public SimpleFileBasedEventStore() throws IOException {
    this(DEFAULT_FILE_NAME);
  }

  public SimpleFileBasedEventStore(String eventStoreFile) throws IOException {
    objectMapper.enableDefaultTypingAsProperty(ObjectMapper.DefaultTyping.NON_FINAL, "type");
    this.eventStoreFile = init(eventStoreFile);
  }

  @Override
  public synchronized List<DomainEvent> loadEvents(final GenericId id) {
    List<DomainEvent> events = new ArrayList<>();
    for (DomainEvent event : getAllEvents()) {
      if (event.aggregateId.equals(id)) {
        events.add(event);
      }
    }
    return events;
  }

  @Override
  public synchronized void save(GenericId id, Class<? extends AggregateRoot> type, List<DomainEvent> events) {
    try {
      for (DomainEvent event : events) {
        Files.append(toString(event), eventStoreFile, UTF_8);
      }
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public synchronized List<DomainEvent> getAllEvents() {
    List<DomainEvent> events = new ArrayList<>();
    try {
      for (String line : Files.readLines(eventStoreFile, UTF_8)) {
        events.add(toEvent(line));
      }
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    return events;
  }

  private DomainEvent toEvent(String json) throws IOException, ClassNotFoundException {
    return objectMapper.readValue(json, DomainEvent.class);
  }

  private String toString(DomainEvent event) throws IOException {
    return objectMapper.writeValueAsString(event) + "\n";
  }

  private File init(String eventStoreFile) throws IOException {

    File file = new File(eventStoreFile);
    if (!file.exists()) {
      if (!file.createNewFile()) {
        throw new IOException("Unable to create file: " + eventStoreFile);
      }
    }
    return file;
  }

}
