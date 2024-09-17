package mate.academy.springbootintro.dto.orderitemdto;

public record OrderItemDto(
        Long id,
        Long bookId,
        int quantity
) {
}
