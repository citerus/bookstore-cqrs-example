package se.citerus.cqrs.bookstore.infrastructure;

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
import static se.citerus.cqrs.bookstore.infrastructure.JsonSerializer.deserialize;
import static se.citerus.cqrs.bookstore.infrastructure.JsonSerializer.serialize;

public class SimpleFileBasedEventStore implements DomainEventStore {

  private final File eventStoreFile;

  public SimpleFileBasedEventStore(File eventStoreFile) throws IOException {
    this.eventStoreFile = createIfNotExist(eventStoreFile);
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

  private DomainEvent toEvent(String line) throws IOException, ClassNotFoundException {
    String className = line.substring(0, line.indexOf("\t"));
    String json = line.substring(line.indexOf("\t")).trim();
    return deserialize(json, (Class<? extends DomainEvent>) Class.forName(className));
  }

  private String toString(DomainEvent event) throws IOException {
    return event.getClass().getName() + "\t" + serialize(event) + "\n";
  }

  private File createIfNotExist(File eventStoreFile) throws IOException {
    if (!eventStoreFile.exists()) {
      if (!eventStoreFile.createNewFile()) {
        throw new IOException("Unable to create file: " + eventStoreFile);
      }
    }
    return eventStoreFile;
  }

}
