package mate.academy.springbootintro.dto.cartitem;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CreateCartItemRequestDto(
        @NotNull @Positive Long bookId,
        @Positive int quantity
) {
}
