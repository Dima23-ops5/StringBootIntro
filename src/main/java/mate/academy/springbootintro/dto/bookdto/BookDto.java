package mate.academy.springbootintro.dto.bookdto;

import java.math.BigDecimal;
import java.util.Set;
import lombok.Data;
import mate.academy.springbootintro.model.Category;

@Data
public class BookDto {
    private Long id;
    private String title;
    private String author;
    private String isbn;
    private BigDecimal price;
    private String description;
    private String coverImage;
    private Set<Category> categories;
}
