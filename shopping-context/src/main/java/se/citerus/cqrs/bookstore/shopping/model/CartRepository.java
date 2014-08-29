package se.citerus.cqrs.bookstore.shopping.model;

public interface CartRepository {

  void save(Cart cart);

  Cart get(String cartId);

  Cart find(String cartId);

  void delete(String cartId);

}
