package mate.academy.springbootintro.dto.cartitem;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record UpdateCartItemRequestDto(
        @NotBlank @Positive int quantity
) {
}
