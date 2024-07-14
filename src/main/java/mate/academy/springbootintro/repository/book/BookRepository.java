package mate.academy.springbootintro.repository.book;

import java.util.List;
import mate.academy.springbootintro.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, Long>, JpaSpecificationExecutor<Book> {
    @Query(value = "SELECT b "
            + "FROM Book b "
            + "JOIN FETCH b.categories c "
            + "WHERE c.id = :id AND b.isDeleted = false")
    List<Book> getBooksByCategoryId(@Param("id") Long id);
}
