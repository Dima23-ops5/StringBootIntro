package mate.academy.springbootintro.mapper;

import mate.academy.springbootintro.config.MapperConfig;
import mate.academy.springbootintro.dto.orderdto.CreateOrderRequestDto;
import mate.academy.springbootintro.dto.orderdto.OrderDto;
import mate.academy.springbootintro.model.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface OrderMapper {
    @Mapping(source = "orderItems", target = "orderItemsDto")
    OrderDto toDto(Order order);

    Order toOrder(CreateOrderRequestDto createOrderRequestDto);
}
