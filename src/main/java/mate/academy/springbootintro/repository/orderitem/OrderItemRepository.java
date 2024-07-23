package mate.academy.springbootintro.repository.orderitem;

import java.util.List;
import java.util.Optional;
import mate.academy.springbootintro.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    @Query(value = "SELECT oi FROM OrderItem oi "
            + "JOIN oi.order o "
            + "WHERE o.id = :orderId AND o.user.id = :userId")
    List<OrderItem> findAllOrderItemsByUserAndOrderIds(
            @Param("orderId") Long orderId, @Param("userId") Long userId
    );

    @Query(value = "SELECT oi FROM OrderItem oi "
            + "JOIN oi.order o "
            + "WHERE oi.id = :itemId AND o.id = :orderId AND o.user.id = :userId")
    Optional<OrderItem> findOrderItemByUserAndOrderAndItemIds(
            @Param("orderId") Long orderId,
            @Param("userId") Long userId,
            @Param("itemId") Long itemId
    );
}
