package mate.academy.springbootintro.dto.shoppingcartdto;

import java.util.Set;
import mate.academy.springbootintro.model.CartItem;

public record ShoppingCartDto(
        Long id,
        Long userId,
        Set<CartItem> cartItemSet
) {
}
