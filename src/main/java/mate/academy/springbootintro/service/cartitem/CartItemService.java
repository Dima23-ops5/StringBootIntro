package mate.academy.springbootintro.service.cartitem;

import mate.academy.springbootintro.dto.cartitem.CartItemDto;
import mate.academy.springbootintro.dto.cartitem.CreateCartItemRequestDto;
import mate.academy.springbootintro.dto.cartitem.UpdateCartItemRequestDto;
import mate.academy.springbootintro.model.ShoppingCart;

public interface CartItemService {
    CartItemDto save(CreateCartItemRequestDto requestDto, ShoppingCart shoppingCart);

    CartItemDto updateCartItem(Long id, UpdateCartItemRequestDto requestDto);

    void deleteCartItem(Long id);
}
