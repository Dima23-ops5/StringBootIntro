package mate.academy.springbootintro.repository.cartitem;

import java.util.Optional;
import mate.academy.springbootintro.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    Optional<CartItem> findByIdAndShoppingCartId(Long itemId, Long cartId);
}
