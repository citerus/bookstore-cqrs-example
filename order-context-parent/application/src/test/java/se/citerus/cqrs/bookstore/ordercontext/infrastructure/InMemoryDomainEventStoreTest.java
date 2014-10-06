package se.citerus.cqrs.bookstore.ordercontext.infrastructure;

import org.junit.Before;
import org.junit.Test;
import se.citerus.cqrs.bookstore.GenericId;
import se.citerus.cqrs.bookstore.domain.AggregateRoot;
import se.citerus.cqrs.bookstore.event.DomainEvent;
import se.citerus.cqrs.bookstore.event.DomainEventStore;
import se.citerus.cqrs.bookstore.ordercontext.application.infrastructure.InMemoryDomainEventStore;

import java.util.Arrays;
import java.util.List;

import static java.lang.System.currentTimeMillis;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class InMemoryDomainEventStoreTest {


  private DomainEventStore eventStore;

  @Before
  public void setUp() {
    eventStore = new InMemoryDomainEventStore();
  }

  @Test
  public void testLoadAndSave() {
    assertThat(eventStore.getAllEvents().isEmpty(), is(true));

    TestId id = new TestId("abc123");
    eventStore.save(id, TestAggregate.class, Arrays.<DomainEvent>asList(new Event1(id, 1, currentTimeMillis())));

    assertThat(eventStore.getAllEvents().size(), is(1));

    eventStore.save(id, TestAggregate.class, Arrays.<DomainEvent>asList(new Event2(id, 2, currentTimeMillis())));
    assertThat(eventStore.getAllEvents().size(), is(2));

    List<DomainEvent> events = eventStore.loadEvents(id);
    assertThat(events.size(), is(2));
  }

  static class TestAggregate extends AggregateRoot<TestId> {
  }

  static public class TestId extends GenericId {
    public TestId(String id) {
      super(id);
    }
  }

  static public class Event1 extends DomainEvent<TestId> {
    public Event1(TestId aggregateId, int version, long timestamp) {
      super(aggregateId, version, timestamp);
    }
  }

  static public class Event2 extends DomainEvent<TestId> {
    public Event2(TestId aggregateId, int version, long timestamp) {
      super(aggregateId, version, timestamp);
    }
  }


}