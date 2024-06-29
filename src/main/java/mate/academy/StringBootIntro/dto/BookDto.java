package mate.academy.StringBootIntro.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BookDto {
    @NotNull
    private Long id;
    @NotNull
    private String title;
    @NotNull
    private String author;
    @NotNull
    private String isbn;
    @NotNull
    private BigDecimal price;
    @NotNull
    private String description;
    @NotNull
    private String coverImage;
}
