package mate.academy.springbootintro.service.order;

import java.util.List;
import mate.academy.springbootintro.dto.orderdto.CreateOrderRequestDto;
import mate.academy.springbootintro.dto.orderdto.OrderDto;
import mate.academy.springbootintro.dto.orderdto.UpdateRequestOrderDto;
import mate.academy.springbootintro.dto.orderitemdto.OrderItemDto;

public interface OrderService {
    OrderDto createOrder(CreateOrderRequestDto createOrderRequestDto, Long userId);

    List<OrderDto> getAllOrders(Long userId);

    OrderDto updateOrderStatus(Long userId, UpdateRequestOrderDto updateRequestOrderDto);

    List<OrderItemDto> getAllOrderItems(Long orderId, Long userId);

    OrderItemDto getOrderItemById(Long orderId, Long itemId, Long userId);
}
