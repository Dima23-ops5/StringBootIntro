package mate.academy.springbootintro.service.shoppingcart;

import mate.academy.springbootintro.dto.cartitem.CartItemDto;
import mate.academy.springbootintro.dto.cartitem.CreateCartItemRequestDto;
import mate.academy.springbootintro.dto.cartitem.UpdateCartItemRequestDto;
import mate.academy.springbootintro.dto.shoppingcartdto.ShoppingCartDto;
import mate.academy.springbootintro.model.User;

public interface ShoppingCartService {
    ShoppingCartDto create(User user);

    ShoppingCartDto findShoppingCardByUserId(Long id);

    CartItemDto addCartItem(CreateCartItemRequestDto cartItemRequestDto,
                            Long currentUserId);

    CartItemDto updateQuantityInCartItem(
            Long cartItemId,
            UpdateCartItemRequestDto updateCartItemRequestDto);

    void deleteCartItem(Long cartItemId, Long userId);
}
