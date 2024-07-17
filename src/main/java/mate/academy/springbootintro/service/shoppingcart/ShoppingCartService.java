package mate.academy.springbootintro.service.shoppingcart;

import mate.academy.springbootintro.dto.cartitem.CreateCartItemRequestDto;
import mate.academy.springbootintro.dto.cartitem.UpdateCartItemRequestDto;
import mate.academy.springbootintro.dto.shoppingcartdto.ShoppingCardDto;

public interface ShoppingCartService {
    ShoppingCardDto findShoppingCardByUserId(Long id);

    ShoppingCardDto addCartItem(CreateCartItemRequestDto cartItemRequestDto,
                                Long currentUserId);

    ShoppingCardDto updateQuantityInCartItem(
            Long cartItemId,
            UpdateCartItemRequestDto updateCartItemRequestDto,
            Long currentUserId);

    void deleteCartItem(Long cartItemId, Long userId);
}
