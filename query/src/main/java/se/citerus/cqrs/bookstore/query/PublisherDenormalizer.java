package se.citerus.cqrs.bookstore.query;

import com.google.common.eventbus.Subscribe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.citerus.cqrs.bookstore.event.DomainEventListener;
import se.citerus.cqrs.bookstore.publisher.PublisherId;
import se.citerus.cqrs.bookstore.publisher.event.PublisherRegisteredEvent;

import java.util.HashMap;
import java.util.Map;

public class PublisherDenormalizer implements DomainEventListener {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  private Map<PublisherId, PublisherProjection> publishers = new HashMap<>();

  @Subscribe
  public void handleEvent(PublisherRegisteredEvent event) {
    logger.info("Received: " + event.toString());
    PublisherProjection projection = new PublisherProjection(event.aggregateId, event.publisherName, event.fee);
    publishers.put(event.aggregateId, projection);
  }

  @Override
  public boolean supportsReplay() {
    return true;
  }

  public PublisherProjection get(PublisherId publisherId) {
    return publishers.get(publisherId);
  }

}
