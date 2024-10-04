package mate.academy.springbootintro.repository.cartitem;

import java.util.Optional;
import java.util.Set;
import mate.academy.springbootintro.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    @Query(value = "UPDATE cart_items c SET c.quantity = :quantity "
            + "WHERE c.shopping_cart_id = :shoppingCartId AND c.id = :cartItemId",
            nativeQuery = true)
    CartItem updateQuantityByShoppingCartIdAndItemId(@Param("shoppingCartId") Long shoppingCartId,
                                                     @Param("cartItemId") Long cartItemId,
                                                     @Param("quantity") int quantity);

    @Query(value = "SELECT * FROM cart_items c "
            + "WHERE c.id = :cartItemId AND c.shopping_cart_id = :shoppingCartId ",
            nativeQuery = true)
    Optional<CartItem> findByIdAndShoppingCartId(@Param("shoppingCartId") Long shoppingCartId,
                                                 @Param("cartItemId") Long cartItemId);

    @Query(value = "SELECT * FROM cart_items c WHERE c.shopping_cart_id = :id ",
            nativeQuery = true)
    Set<CartItem> findAllByShoppingCartId(@Param(value = "id") Long id);
}
