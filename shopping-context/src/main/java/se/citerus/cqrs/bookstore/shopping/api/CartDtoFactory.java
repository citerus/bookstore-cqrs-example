package se.citerus.cqrs.bookstore.shopping.api;

import se.citerus.cqrs.bookstore.shopping.model.Cart;
import se.citerus.cqrs.bookstore.shopping.model.Item;
import se.citerus.cqrs.bookstore.shopping.model.LineItem;

import java.util.ArrayList;
import java.util.List;

public class CartDtoFactory {

  public static CartDto fromCart(Cart cart) {
    long totalCartPrice = 0;
    int totalCartQuantity = 0;
    List<LineItemDto> lineItems = new ArrayList<>();

    for (LineItem lineItem : cart.getItems()) {
      long price = lineItem.getTotalPrice();
      int quantity = lineItem.getQuantity();

      totalCartPrice += price;
      totalCartQuantity += quantity;

      Item item = lineItem.getItem();
      LineItemDto itemDto = new LineItemDto();
      itemDto.bookId = item.bookId.id;
      itemDto.title = item.title;
      itemDto.price = item.price;
      itemDto.quantity = quantity;
      itemDto.totalPrice = price;
      lineItems.add(itemDto);
    }

    CartDto cartDto = new CartDto();
    cartDto.cartId = cart.cartId;
    cartDto.totalPrice = totalCartPrice;
    cartDto.totalQuantity = totalCartQuantity;
    cartDto.lineItems = lineItems;
    return cartDto;
  }

}
