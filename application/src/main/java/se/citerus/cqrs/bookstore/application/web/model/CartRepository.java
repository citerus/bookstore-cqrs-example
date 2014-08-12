package se.citerus.cqrs.bookstore.application.web.model;

public interface CartRepository {

  void save(Cart cart);

  Cart get(String cartId);

  Cart find(String cartId);

  void delete(String cartId);

}
