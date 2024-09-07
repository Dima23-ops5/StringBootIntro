package mate.academy.springbootintro.repository.cartitem;

import java.util.Set;
import mate.academy.springbootintro.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    @Query(value = "SELECT * FROM cart_items c WHERE c.shopping_cart_id = :id ",
            nativeQuery = true)
    Set<CartItem> findAllByShoppingCartId(@Param(value = "id") Long id);
}
