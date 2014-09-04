package se.citerus.cqrs.bookstore.shopping.domain;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class Cart {

  public final String cartId;

  private final Map<ProductId, LineItem> lineItems = new LinkedHashMap<>();

  public Cart(String cartId) {
    this.cartId = cartId;
  }

  public void add(Item item) {
    LineItem lineItem = lineItems.get(item.productId);
    if (lineItem == null) {
      lineItem = new LineItem(item);
    } else {
      lineItem.increaseQuantity();
    }
    lineItems.put(item.productId, lineItem);
  }

  public Collection<LineItem> getItems() {
    return Collections.unmodifiableCollection(lineItems.values());
  }

  public int getLineCount() {
    return lineItems.size();
  }

  public long getTotalAmount() {
    long totalAmount = 0;
    for (LineItem lineItem : lineItems.values()) {
      totalAmount += lineItem.getTotalPrice();
    }
    return totalAmount;
  }

  public void remove(ProductId productId) {
    LineItem lineItem = lineItems.get(productId);
    if (lineItem != null) {
      lineItem.decreaseQuantity();
      if (lineItem.getQuantity() == 0) {
        lineItems.remove(productId);
      }
    }
  }

  public void removeAll(ProductId productId) {
    lineItems.remove(productId);
  }

}
