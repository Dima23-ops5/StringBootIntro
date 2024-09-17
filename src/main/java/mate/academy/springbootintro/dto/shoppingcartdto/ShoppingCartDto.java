package mate.academy.springbootintro.dto.shoppingcartdto;

import java.util.Set;
import mate.academy.springbootintro.dto.cartitem.CartItemDto;

public record ShoppingCartDto(
        Long id,
        Long userId,
        Set<CartItemDto> cartItemSet
) {
}
