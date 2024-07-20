package mate.academy.springbootintro.service.order.impl;

import jakarta.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import mate.academy.springbootintro.dto.orderdto.CreateOrderRequestDto;
import mate.academy.springbootintro.dto.orderdto.OrderDto;
import mate.academy.springbootintro.dto.orderdto.UpdateRequestOrderDto;
import mate.academy.springbootintro.dto.orderitemdto.OrderItemDto;
import mate.academy.springbootintro.mapper.OrderItemMapper;
import mate.academy.springbootintro.mapper.OrderMapper;
import mate.academy.springbootintro.model.Order;
import mate.academy.springbootintro.model.OrderItem;
import mate.academy.springbootintro.model.ShoppingCart;
import mate.academy.springbootintro.repository.order.OrderRepository;
import mate.academy.springbootintro.repository.orderitem.OrderItemRepository;
import mate.academy.springbootintro.repository.shoppingcart.ShoppingCartRepository;
import mate.academy.springbootintro.service.order.OrderService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;
    private final ShoppingCartRepository shoppingCartRepository;
    private final OrderItemRepository orderItemRepository;

    @Override
    public OrderDto createOrder(CreateOrderRequestDto createOrderRequestDto, Long userId) {
        ShoppingCart cart = shoppingCartRepository.findByUserId(userId);
        Order order = orderMapper.toOrder(createOrderRequestDto);
        Set<OrderItem> orderItems = cart.getCartItems().stream()
                .map(cartItem -> orderItemMapper.toOrderItem(cartItem, order.getId()))
                .collect(Collectors.toSet());
        order.setOrderItems(orderItems);
        order.setOrderDate(LocalDateTime.now());
        order.setUser(cart.getUser());
        order.setTotal(orderItems.stream()
                .map(orderItem -> orderItem.getPrice())
                .reduce(BigDecimal.ZERO,BigDecimal::add));
        order.setStatus(Order.StatusName.PENDING);
        return orderMapper.toDto(orderRepository.save(order));
    }

    @Override
    public List<OrderDto> getAllOrders(Long userId) {
        return orderRepository.findAll().stream()
                .map(order -> orderMapper.toDto(order))
                .toList();
    }

    @Override
    public OrderDto updateOrderStatus(
            Long id,
            UpdateRequestOrderDto updateRequestOrderDto
    ) {
        Order order = orderRepository.findById(id)
                .orElseThrow(
                        () -> new EntityNotFoundException(
                                String.format("Cannot found order with id: %d", id)
                        )
                );
        order.setStatus(Enum.valueOf(Order.StatusName.class, updateRequestOrderDto.status()));
        return orderMapper.toDto(
                orderRepository.save(
                        order
                )
        );
    }

    @Override
    public List<OrderItemDto> getAllOrderItems(Long orderId, Long userId) {
        return orderItemRepository.findAllOrderItemsByUserAndOrderIds(orderId, userId).stream()
                .map(orderItem -> orderItemMapper.toDto(orderItem)).toList();
    }

    @Override
    public OrderItemDto getOrderItemById(Long orderId, Long itemId, Long userId) {
        OrderItem orderItem = orderItemRepository.findOrderItemByUserAndOrderAndItemIds(
                orderId, itemId, userId
        ).orElseThrow(
                () -> new EntityNotFoundException(
                        String.format(
                                "Cannot found order item with user id: %d ,"
                                        + " order id: %d and item id: %d",
                                userId, orderId, itemId
                        )
                )
        );
        return orderItemMapper.toDto(orderItem);
    }
}
