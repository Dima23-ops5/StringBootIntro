package mate.academy.springbootintro.service.shoppingcart;

import mate.academy.springbootintro.dto.cartitem.CreateCartItemRequestDto;
import mate.academy.springbootintro.dto.cartitem.UpdateCartItemRequestDto;
import mate.academy.springbootintro.dto.shoppingcartdto.ShoppingCartDto;
import mate.academy.springbootintro.model.User;

public interface ShoppingCartService {
    ShoppingCartDto create(User user);
    ShoppingCartDto findShoppingCardByUserId(Long id);

    ShoppingCartDto addCartItem(CreateCartItemRequestDto cartItemRequestDto,
                                Long currentUserId);

    ShoppingCartDto updateQuantityInCartItem(
            Long cartItemId,
            UpdateCartItemRequestDto updateCartItemRequestDto,
            Long currentUserId);

    void deleteCartItem(Long cartItemId, Long userId);
}
