package se.citerus.cqrs.bookstore.shopping.infrastructure;

import se.citerus.cqrs.bookstore.shopping.domain.Cart;
import se.citerus.cqrs.bookstore.shopping.domain.CartRepository;

import java.util.concurrent.ConcurrentHashMap;

import static com.google.common.base.Preconditions.checkArgument;
import static java.lang.String.format;

public class InMemoryCartRepository implements CartRepository {

  private ConcurrentHashMap<String, Cart> sessions = new ConcurrentHashMap<>();

  public void save(Cart cart) {
    if (sessions.putIfAbsent(cart.cartId, cart) != null) {
      throw new IllegalArgumentException(format("Shopping cart with id '%s' already exists", cart.cartId));
    }
  }

  public Cart get(String cartId) {
    Cart cart = sessions.get(cartId);
    checkArgument(cart != null, "No shopping cart with id '%s' exists", cartId);
    return cart;
  }

  public Cart find(String cartId) {
    return sessions.get(cartId);
  }

  public void delete(String cartId) {
    sessions.remove(cartId);
  }

}
