package mate.academy.StringBootIntro.dto;

import java.math.BigDecimal;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateBookRequestDto {
    @NotBlank
    @NotNull(message = "Title can`t be null")
    private String title;
    @NotBlank
    @NotNull(message = "Author can`t be null")
    private String author;
    @NotBlank
    @NotNull(message = "Isbn can`t be null")
    private String isbn;
    @NotNull(message = "Price can`t be null and also will be greater than 0")
    private BigDecimal price;
    @NotBlank
    @NotNull(message = "Description can`t be null")
    private String description;
    @NotBlank
    @NotNull(message = "CoverImage can`t be null")
    private String coverImage;
}
