package mate.academy.StringBootIntro.model;

import java.math.BigDecimal;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "books")
@Getter
@Setter
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String author;
    @Column(nullable = false,unique = true)
    private String isbn;
    @Column(nullable = false)
    private BigDecimal price;
    private String description;
    private String coverImage;

    @Override
    public String toString() {
        return "Book {"
                + "id=" + id
                + ", title=" + title
                + ", author=" + author
                + ", isbn=" + isbn
                + ", price=" + price
                + ", description=" + description
                + ", coverImage=" + coverImage
                + "}";
    }
}
