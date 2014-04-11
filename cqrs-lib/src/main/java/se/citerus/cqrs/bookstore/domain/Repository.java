package se.citerus.cqrs.bookstore.domain;

import se.citerus.cqrs.bookstore.GenericId;

public interface Repository {

  <AR extends AggregateRoot> void save(AR aggregateRoot);

  <AR extends AggregateRoot, ID extends GenericId> AR load(ID id, Class<AR> aggregateType);

}
