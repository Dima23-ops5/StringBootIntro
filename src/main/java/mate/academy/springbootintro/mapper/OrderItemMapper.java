package mate.academy.springbootintro.mapper;

import java.math.BigDecimal;
import mate.academy.springbootintro.config.MapperConfig;
import mate.academy.springbootintro.dto.orderitemdto.OrderItemDto;
import mate.academy.springbootintro.model.CartItem;
import mate.academy.springbootintro.model.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(config = MapperConfig.class)
public interface OrderItemMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(source = "cartItem", target = "price", qualifiedByName = "getPrice")
    OrderItem toOrderItem(CartItem cartItem, Long orderId);

    OrderItemDto toDto(OrderItem orderItem);

    @Named("getPrice")
    default BigDecimal getPrice(CartItem cartItem) {
        return BigDecimal.valueOf(cartItem.getQuantity())
                .multiply(cartItem.getBook().getPrice());
    }
}
