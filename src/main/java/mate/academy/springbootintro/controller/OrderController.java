package mate.academy.springbootintro.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Positive;
import java.util.List;
import lombok.RequiredArgsConstructor;
import mate.academy.springbootintro.dto.orderdto.CreateOrderRequestDto;
import mate.academy.springbootintro.dto.orderdto.OrderDto;
import mate.academy.springbootintro.dto.orderdto.UpdateRequestOrderDto;
import mate.academy.springbootintro.dto.orderitemdto.OrderItemDto;
import mate.academy.springbootintro.model.User;
import mate.academy.springbootintro.service.order.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
@Validated
@Tag(
        name = "Order management",
        description = "Endpoints for mapping orders"
)
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('USER')")
    @Operation(
            summary = "Create an order",
            description = "Creating a new order"
    )
    public OrderDto createOrder(
            @RequestBody @Validated CreateOrderRequestDto createOrderRequestDto,
            Authentication authentication
    ) {
        Long userId = getCurrentUserId(authentication);
        return orderService.createOrder(createOrderRequestDto, userId);
    }

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    @Operation(
            summary = "History of orders",
            description = "Getting all orders that had user"
    )
    public List<OrderDto> getHistoryOfOrders(Authentication authentication) {
        Long userId = getCurrentUserId(authentication);
        return orderService.getAllOrders(userId);
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Update order",
            description = "Update order status by order id"
    )
    public OrderDto updateOrder(
            @PathVariable @Positive Long id,
            @RequestBody UpdateRequestOrderDto updateRequestOrderDto
    ) {
        return orderService.updateOrderStatus(id, updateRequestOrderDto);
    }

    @GetMapping("/{orderId}/items")
    @PreAuthorize("hasRole('USER')")
    @Operation(
            summary = "Get all order items",
            description = "Getting all order that contains order with order id")
    public List<OrderItemDto> getAllItems(
            @PathVariable @Positive Long orderId,
            Authentication authentication
    ) {
        Long userId = getCurrentUserId(authentication);
        return orderService.getAllOrderItems(orderId, userId);
    }

    @GetMapping("/{orderId}/items/{itemId}")
    @PreAuthorize("hasRole('USER')")
    @Operation(
            summary = "Get order item by id",
            description = "Getting order item by item id and order id"
    )
    public OrderItemDto getOrderItemById(
            @PathVariable @Positive Long orderId,
            @PathVariable @Positive Long itemId,
            Authentication authentication
    ) {
        Long userId = getCurrentUserId(authentication);
        return orderService.getOrderItemById(orderId, itemId, userId);
    }

    private Long getCurrentUserId(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return user.getId();
    }
}
