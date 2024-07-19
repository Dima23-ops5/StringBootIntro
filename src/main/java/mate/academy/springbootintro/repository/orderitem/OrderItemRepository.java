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
    List<OrderItem> findAllByUserId(Long userId);

    @Query(value = "SELECT items FROM OrderItems items "
            + "JOIN items.order order "
            + "JOIN order.user user "
            + "WHERE order.id = :orderId AND user.id = :userId ")
    List<OrderItem> findAllOrderItemsByUserAndOrderIds(
            @Param("orderId") Long orderId, @Param("userId") Long userId
    );

    @Query(value = "SELECT items FROM OrderItems items "
            + "JOIN items.order order "
            + "JOIN order.user user "
            + "WHERE items.id = :itemId AND order.id = :orderId AND user.id = :userId")
    Optional<OrderItem> findOrderItemByUserAndOrderAndItemIds(
            @Param("orderId") Long orderId,
            @Param("userId") Long userId,
            @Param("itemId") Long itemId
    );

}
