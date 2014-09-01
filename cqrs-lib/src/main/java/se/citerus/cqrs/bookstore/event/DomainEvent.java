package se.citerus.cqrs.bookstore.event;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import se.citerus.cqrs.bookstore.GenericId;

import java.io.Serializable;

import static org.apache.commons.lang3.builder.ToStringStyle.SHORT_PREFIX_STYLE;

/**
 * Base class for all events. Makes sure they are all comparable by their values.
 */
public abstract class DomainEvent<T extends GenericId> implements Serializable {

  public final T aggregateId;
  public final int version;
  public final long timestamp;

  protected DomainEvent(T aggregateId, int version, long timestamp) {
    this.aggregateId = aggregateId;
    this.version = version;
    this.timestamp = timestamp;
  }

  @Override
  public boolean equals(Object o) {
    return EqualsBuilder.reflectionEquals(this, o);
  }

  @Override
  public int hashCode() {
    return HashCodeBuilder.reflectionHashCode(this);
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this, SHORT_PREFIX_STYLE);
  }

}
