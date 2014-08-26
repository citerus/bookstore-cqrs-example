package se.citerus.cqrs.bookstore.shopping.web.model;

import se.citerus.cqrs.bookstore.ValueObject;

public class LineItem extends ValueObject {

  private final Item item;
  private int quantity;

  public LineItem(Item item) {
    this.item = item;
    this.quantity = 1;
  }

  public void increaseQuantity() {
    this.quantity++;
  }

  public void decreaseQuantity() {
    if (quantity > 0) quantity--;
  }

  public int getQuantity() {
    return quantity;
  }

  public long getPrice() {
    return item.price;
  }

  public long getTotalPrice() {
    return item.price * quantity;
  }

  public Item getItem() {
    return item;
  }

}
