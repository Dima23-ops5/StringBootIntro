package mate.academy.StringBootIntro.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import java.math.BigDecimal;

@Entity
@SQLDelete(sql = "UPDATE books SET is_delete = true WHERE id=?")
@SQLRestriction(value = "is_delete=false")
@Getter
@Setter
@Table(name = "books")
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
    @Column(nullable = false)
    private boolean is_delete = false;

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
