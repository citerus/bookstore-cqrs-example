package se.citerus.cqrs.bookstore.shopping.api;

import se.citerus.cqrs.bookstore.shopping.domain.Cart;
import se.citerus.cqrs.bookstore.shopping.domain.Item;
import se.citerus.cqrs.bookstore.shopping.domain.LineItem;

import java.util.ArrayList;
import java.util.List;

public class CartDtoFactory {

  public static CartDto fromCart(Cart cart) {
    List<LineItemDto> lineItems = new ArrayList<>();

    for (LineItem lineItem : cart.getItems()) {
      LineItemDto itemDto = toLineItemDto(lineItem);
      lineItems.add(itemDto);
    }

    CartDto cartDto = new CartDto();
    cartDto.cartId = cart.cartId;
    cartDto.totalPrice = cart.getTotalPrice();
    cartDto.totalQuantity = cart.getTotalQuantity();
    cartDto.lineItems = lineItems;
    return cartDto;
  }

  private static LineItemDto toLineItemDto(LineItem lineItem) {
    long price = lineItem.getTotalPrice();
    int quantity = lineItem.getQuantity();
    Item item = lineItem.getItem();
    LineItemDto itemDto = new LineItemDto();
    itemDto.productId = item.productId.id;
    itemDto.title = item.title;
    itemDto.price = item.price;
    itemDto.quantity = quantity;
    itemDto.totalPrice = price;
    return itemDto;
  }

}
