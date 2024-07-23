package mate.academy.springbootintro.repository.order;

import mate.academy.springbootintro.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Order findByUserId(Long userId);
}
