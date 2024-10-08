package mate.academy.springbootintro.dto.bookdto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.util.List;
import lombok.Data;

@Data
public class CreateBookRequestDto {
    @NotBlank(message = "Title can`t be null")
    private String title;
    @NotBlank(message = "Author can`t be null")
    private String author;
    @NotBlank(message = "Isbn can`t be null")
    private String isbn;
    @Positive(message = "Price can`t be null and also will be greater than 0")
    private BigDecimal price;
    @NotBlank(message = "Description can`t be null")
    private String description;
    @NotBlank(message = "Cover image can`t be null")
    private String coverImage;
    private List<Long> categoryIds;
}
