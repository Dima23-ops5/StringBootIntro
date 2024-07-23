package mate.academy.springbootintro.dto.orderdto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;
import mate.academy.springbootintro.dto.orderitemdto.OrderItemDto;

public record OrderDto(
        Long id,
        Long user_id,
        Set<OrderItemDto> orderItemsDto,
        LocalDateTime orderDate,
        BigDecimal total,
        String status
) {
}
