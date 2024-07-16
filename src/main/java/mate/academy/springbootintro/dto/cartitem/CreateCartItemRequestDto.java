package mate.academy.springbootintro.dto.cartitem;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record CreateCartItemRequestDto(
        @NotBlank @Positive Long bookId,
        @NotBlank int quantity
) {
}
