package mate.academy.springbootintro.dto.orderdto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateOrderRequestDto(
        @NotBlank @Size(min = 5, max = 50) String shippingAddress
) {
}
