package mate.academy.springbootintro.repository.shoppingcart;

import mate.academy.springbootintro.model.ShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Long> {
    @Query("FROM ShoppingCart s WHERE s.user.id = :id")
    ShoppingCart findByUserId(@Param("id") Long userId);
}
