package mate.academy.springbootintro.dto.bookdto;

import java.math.BigDecimal;

public record BookDtoWithoutCategoryIds(
        Long id,
        String title,
        String author,
        String isbn,
        BigDecimal price,
        String description,
        String coverImage
) {
}
