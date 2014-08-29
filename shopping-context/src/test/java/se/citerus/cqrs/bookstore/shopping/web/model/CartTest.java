package se.citerus.cqrs.bookstore.shopping.web.model;

import org.junit.Test;

import java.util.UUID;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class CartTest {

  private String cartId = UUID.randomUUID().toString();

  @Test
  public void testAddSingleItemToCart() {
    Cart cart = new Cart(cartId);
    cart.add(new Item(BookId.<BookId>randomId(), "Item1", 1000));

    assertThat(cart.getItems().size(), is(1));
    assertThat(cart.getTotalAmount(), is(1000L));
  }

  @Test
  public void testAddMultipleItemsOfSameKindToCart() {
    Cart cart = new Cart(cartId);
    Item item = new Item(BookId.<BookId>randomId(), "Item1", 1000);
    cart.add(item);
    cart.add(item);

    assertThat(cart.getItems().size(), is(1));
    assertThat(cart.getTotalAmount(), is(2000L));
  }

  @Test
  public void testAddMultipleItemsOfDifferentKindToCart() {
    Cart cart = new Cart(cartId);
    cart.add(new Item(BookId.<BookId>randomId(), "Item1", 1000));
    cart.add(new Item(BookId.<BookId>randomId(), "Item2", 1000));

    assertThat(cart.getItems().size(), is(2));
    assertThat(cart.getTotalAmount(), is(2000L));
  }

  @Test
  public void testRemoveSingleItemFromCart() {
    Cart cart = new Cart(cartId);

    BookId bookId = BookId.randomId();
    cart.add(new Item(bookId, "Item", 1000));

    cart.remove(bookId);

    assertThat(cart.getItems().isEmpty(), is(true));
    assertThat(cart.getTotalAmount(), is(0L));
  }

  @Test
  public void testRemoveAllItemsFromCart() {
    Cart cart = new Cart(cartId);

    BookId bookId = BookId.randomId();
    Item item = new Item(bookId, "Item", 1000);
    cart.add(item);
    cart.add(item);

    cart.removeAll(bookId);

    assertThat(cart.getItems().isEmpty(), is(true));
    assertThat(cart.getTotalAmount(), is(0L));
  }

}
